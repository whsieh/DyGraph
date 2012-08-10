package dygraph;

import gui.graph.AbstractPainter;
import gui.graph.EdgePainter;
import gui.graph.GraphData;
import gui.graph.GraphData.IEdgeData;
import gui.graph.GraphData.IVertexData;
import gui.graph.GraphViewer;
import gui.graph.VertexPainter;
import gui.graph.util.Message;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import model.graph.Edge;
import model.graph.Vertex;
import util.misc.Vector2D;

import com.restfb.types.Post;

import dygraph.DygraphController.Mode;
import dygraph.FacebookGraphData.FacebookEdgeData;
import dygraph.FacebookGraphData.FacebookVertexData;
import dygraph.compare.CompareFrame;

public class DygraphViewer extends GraphViewer {

	private final static BasicStroke GHOST_PROFILE_STROKE = new BasicStroke(2.0f); 
	private final static AlphaComposite ALPHA_COMP = 
			AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
	
	protected Map<String,ProfileQueryEngine> profileSet;
	protected DygraphController dController;
	
	// this goes down as one of the worst variable names ever -.-
	protected FacebookVertexPainter currentlyGhosted;
	
	protected CompareFrame favoritesFrame;
	
	public DygraphViewer(DygraphController controller) {
		super(controller);
		profileSet = new HashMap<String,ProfileQueryEngine>();
		dController = (DygraphController)controller;
	}
	
	@Override
	public void addGraphData(GraphData<? extends IVertexData,? extends IEdgeData> data) {
		if (data instanceof FacebookGraphData) {
			addFacebookGraphData((FacebookGraphData)data);
		} else {
			super.addGraphData(data);
		}
	}
	
	private void drawGhostProfileImage(Graphics2D g2d, boolean opaque) {
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Composite c = g2d.getComposite();
		Stroke s = g2d.getStroke();
		if (!opaque) {
			g2d.setComposite(ALPHA_COMP);
		}
		g2d.setStroke(GHOST_PROFILE_STROKE);
		// int halfWidth = currentlyGhosted.width/2,halfHeight = currentlyGhosted.height/2;
		g2d.drawImage(currentlyGhosted.image, curX-currentlyGhosted.width, curY-currentlyGhosted.height, this);
		g2d.drawRoundRect(curX-currentlyGhosted.width, curY-currentlyGhosted.height, currentlyGhosted.width, currentlyGhosted.height,5,5);
		g2d.setComposite(c);
		g2d.setStroke(s);
	}
	
	@Override
	public void paintFrame(Graphics g) {
		
		Mode currentMode = dController.getMode();
		
		Graphics2D g2d = (Graphics2D)g;
		super.paintFrame(g2d);
		/* Draw special "decorations" depending on the mode type */
		switch(currentMode) {
				
			case SEARCH:
				g2d.drawImage(DygraphResource.GREEN_PLUS, curX+10, curY+10, this);
				break;
			
			case DRAG_DROP:
				VertexPainter vp = locateVertexPainter(curX, curY);
				drawGhostProfileImage(g2d,vp != null && vp != currentlyGhosted);
				break;
			
			case DEFAULT:
				break;
				
		}
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
	}
	
	protected void fadeInViewerBackground() {
		new Thread(new Runnable() {
			final DygraphViewer viewer = dController.dView;
			int color = 105;
			@Override
			public void run() {
				while(color < 255) {
					color += 3;
					viewer.setBackground(new Color(color,color,color));
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						
					}
				}
			}
		}).start();
	}
	
	protected void fadeOutViewerBackground() {
		new Thread(new Runnable() {
			final DygraphViewer viewer = dController.dView;
			int color = 255;
			@Override
			public void run() {
				while(color > 105) {
					color -= 3;
					viewer.setBackground(new Color(color,color,color));
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						
					}
				}
			}
		}).start();
	}
	
	protected void openFavoritesPanel(final String name1, final String name2) {
		new Thread(new Runnable() {
			private int width = 100,height = 60;
			@Override
			public void run() {
				favoritesFrame = new CompareFrame(
						halfWidth-width/2,halfHeight-height/2,width,height);
				add(favoritesFrame);
				favoritesFrame.setTitle(name1 + " and " + name2);
				favoritesFrame.expand(1200+width,1200+height);
			}
		}).start();
	}
	
	protected void closeFavoritesPanel() {
		new Thread(new Runnable() {
			private int width = 100,height = 60;
			@Override
			public void run() {
				/* Run closing animation here... */
				favoritesFrame.setVisible(false);
				remove(favoritesFrame);
				favoritesFrame = null;
			}
		}).start();
	}
	
	public String whois(String id) {
		FacebookVertexPainter vp = (FacebookVertexPainter)vertexPainterMap.get(id);
		if (vp != null) {
			return vp.getDisplayName();
		}
		return "<nobody>";
	}
	
	public ProfileQueryEngine getProfile(String id) {
		ProfileQueryEngine profile = profileSet.get(id);
		if (profile == null) {
			profile = new ProfileQueryEngine(id);
			profileSet.put(id,profile);
		}
		return profile;
	}
	
	@Override
	protected void createContextMenu() {
		
		WHITESPACE_POPUPMENU = new JPopupMenu();
        VERTEX_POPUPMENU = new JPopupMenu();
        EDGE_POPUPMENU = new JPopupMenu();
        
        WHITESPACE_MENUITEMS = new JMenuItem[] { /*new JMenuItem("Prune connections")*/};
        VERTEX_MENUITEMS = new JMenuItem[] {
        		new JMenuItem("Expand connections"),new JMenuItem("Visit profile")};
        EDGE_MENUITEMS = new JMenuItem[] {new JMenuItem("See friendship")};
        
        for(int i = 0; i < VERTEX_MENUITEMS.length; i++) {
            VERTEX_POPUPMENU.add(VERTEX_MENUITEMS[i]);
        }
        for(int i = 0; i < EDGE_MENUITEMS.length; i++) {
            EDGE_POPUPMENU.add(EDGE_MENUITEMS[i]);
        }
        for(int i = 0; i < WHITESPACE_MENUITEMS.length; i++) {
        	WHITESPACE_POPUPMENU.add(WHITESPACE_MENUITEMS[i]);
        }
        this.setBackground(Color.WHITE);
        
        /*
        WHITESPACE_MENUITEMS[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	pruneConnections();
            }
        });
        */
        VERTEX_MENUITEMS[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FacebookVertexPainter fbVertex = ((FacebookVertexPainter)getCurrentlySelected());
                if (fbVertex == null) {
                	fbVertex = ((FacebookVertexPainter)getCurrentlyFocused());
                }
                if (fbVertex != null) {
                	expandProfileConnections(fbVertex.getID(),5);
                }
            }
        });
        VERTEX_MENUITEMS[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FacebookVertexPainter fbVertex = ((FacebookVertexPainter)getCurrentlySelected());
                if (fbVertex == null) {
                	fbVertex = ((FacebookVertexPainter)getCurrentlyFocused());
                }
                ((DygraphController)controller).popURL("http://www.facebook.com/" + fbVertex.getID());
            }
        });
        EDGE_MENUITEMS[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	VertexPainter[] vertices = ((EdgePainter)getCurrentlySelected()).getConnectingVertices();
            	FacebookVertexPainter v0 = (FacebookVertexPainter)vertices[0];
            	FacebookVertexPainter v1 = (FacebookVertexPainter)vertices[1];
            	((DygraphController)controller).popURL("http://www.facebook.com/" +
            			getProfile(v0.getID()).user.getUsername() + "?and=" + v1.getID());
            }
        });
	}
	
	@Override
	public FacebookVertexPainter locateVertexPainter(int x, int y) {
		return (FacebookVertexPainter)vertexTable.find(new Point(x,y));
	}
	
	protected void pruneConnections() {
		synchronized(this) {
			int deltaCount;
			do {
				deltaCount = 0;
				for (String id : graph.vertices()) {
					Vertex v = graph.findVertex(id);
					FacebookVertexPainter fvp = (FacebookVertexPainter)vertexPainterMap.get(id);
					if (v.weight() <= 1.0 && !id.equals(ProfileQueryEngine.CURRENT_USER.key())
							&& fvp != null && !fvp.isLoading) {
						deltaCount++;
						removeVertex(id);
					}
				}
			} while(deltaCount > 0);
		}
	}
	
	protected FacebookVertexPainter getFacebookProfilePainter(String id) {
		return (FacebookVertexPainter)vertexPainterMap.get(id);
	}
	
	@Override
	protected VertexPainter createVertexPainter(int x, int y, String id, String displayName) {
		return new FacebookVertexPainter(this,x,y,id,displayName);
	}
	
	public void expandProfileConnections(final String id) {
		expandProfileConnections(id, 1);
	}
	
	public void expandProfileConnections(final String id, final int count) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				FacebookVertexPainter fbvp = (FacebookVertexPainter)vertexPainterMap.get(id);
				if (fbvp != null && !fbvp.isLoading) {
					try {
						fbvp.setLoading(true);
						ProfileQueryEngine engine = getProfile(id);
						if (engine != null) {
							for (int i = 0; i < count; i++) {
								for (Post post : engine.fetchNextPosts()) {
									FacebookGraphData data = FacebookUtil.toGraphData(post);
									addFacebookGraphData(data);
								}
							}
						}
						fbvp.setLoading(false);
					} catch (NullPointerException e) {
						System.err.println("An error occurred while expanding connections for "
								+ fbvp.getDisplayName());
						fbvp.setLoading(false);
					} catch (NoSuchElementException e) {
						System.err.println("No further connections detected for "
								+ fbvp.getDisplayName());
						fbvp.setLoading(false);
					}
				}
			}
		}).start();
	}
	
    private void addFacebookGraphData(FacebookGraphData data) {
    	
    	for (FacebookVertexData vd : data.getVertexInfo()) {
    		String vid = vd.getID();
    		if (graph.findVertex(vid) == null) {
    			int[] randomPoints = GraphViewer.getRandomPoints();
    			if (DygraphConsole.exists()) {
    				DygraphConsole.getInstance().log("Adding user " + vid + " (" + vd.getName() + ")");
    			}
    			addVertex(vid, randomPoints[0], randomPoints[1],vd.getName());
    		}
    	}
    	
    	for (FacebookEdgeData ed : data.getEdgeInfo()) {
    		String eid = ed.getID();
    		String[] vid = ed.getVertexID();
    		String msg = ed.getMessage();
    		if (graph.findVertex(vid[0]) != null && graph.findVertex(vid[1]) != null) {
    			VertexPainter vp1 = vertexPainterMap.get(vid[0]);
    			VertexPainter vp2 = vertexPainterMap.get(vid[1]);
    			EdgePainter ep = edgePainterMap.get(eid);
    			Edge e = graph.findEdge(eid);
    			List<String> postData = null;
	    		if (e == null && ep == null) {
	    			ep = addEdge(eid,vp1,vp2,ed.weight());
	    			e = graph.findEdge(eid);
	    			postData = (List<String>)e.setData("post",new LinkedList<String>());
	    		} else if (e != null && ep != null){
	    			e.addWeight(ed.weight());
	    			ep.addWeight(ed.weight());
	    			postData = (List<String>)e.getData("post");
	    		}
	    		if (postData != null) {
	    			postData.add(msg);
	    		}
    		}
    	}
    }
    
    protected void initiatePhysics() {
        if (RUN_PHYSICS) {
        	new Thread(new DygraphPhysicsSimulator()).start();
        }
    }
    
    protected class DygraphAnimator extends Animator {
    	
	    @Override
	    public void run() {
	        Graphics2D g2d = (Graphics2D)getGraphics();
	        while(true) {
	        	if (dController.getMode() != Mode.COMPARE) {
		            repaint();
		            try {
		                Thread.sleep(5);
		            } catch (InterruptedException e) {
		            }
	        	}
	            // System.out.println("Animation: " + (System.nanoTime() - start)/1000000.0);
	        }
	    }    	
    }
	
    protected class DygraphPhysicsSimulator extends GraphPhysicsSimulator {
    	
	    @Override
	    public void run() {
	        while(true) {
	        	if (dController.getMode() != Mode.COMPARE) {
		            int ELAPSED_MS = (int)(runPhysicsCycle()/1000000.0);
		            // System.out.println("Physics: " + ELAPSED_MS + " ms");
		            if (ELAPSED_MS < DEFAULT_FRAME_TIME_MS) {
		                try{
		                    Thread.sleep(DEFAULT_FRAME_TIME_MS - ELAPSED_MS);
		                }catch(Exception e){
		                    e.printStackTrace();
		                    System.exit(1);
		                }
		            }
	        	}
	        }
	    }
	    
	    protected void handleVelocityThreshold() {
	    	if (dController.mode != Mode.DRAG_DROP && currentlyDragged != null
	    			&& currentlyDragged instanceof VertexPainter) {
        		VertexPainter vp = (VertexPainter)currentlyDragged;
        		Vertex v = graph.findVertex(vp.getID());
        		double magn = vp.acceleration.calcMagnitude() / v.degree();
        		if (magn > 0.015) {
        			currentlyGhosted = (FacebookVertexPainter)vp;
    				curX = currentlyGhosted.x+currentlyGhosted.width/2;
    				curY = currentlyGhosted.y+currentlyGhosted.height/2;
        			dController.mode = Mode.DRAG_DROP;
        			currentlyDragged.inform(Message.MOUSE_DESELECTED,null);
        			currentlyDragged = null;
        			currentlySelected = null;
        			draggingView = true;
    			}
	    	}
	    }
	    
	    protected void calcAllRepulsiveForces() {
	        for(VertexPainter m1 : vertexList) {
	            for(VertexPainter m2 : vertexList) {
	                if (m1 != m2) {
	                    m1.acceleration.add(repulsiveForce(m1,m2).
	                            scaleTo(-1/m1.mass()));
	                    if (hasFocus() && !draggingView && currentlyDragged == null
	                    		&& dController.mode != Mode.COMPARE) {
		                    Vector2D dragForce = autoDragForce();
		                    if (dragForce != null) {
		                    	m1.acceleration.add(dragForce.scaleTo(-1/m1.mass()));
		                    }
	                    }
	                    handleVelocityThreshold();
	                }
	            }
	            m1.calc(DEFAULT_TIMESTEP_MS);
	        }
	    }
	    
	}
    
	public AbstractPainter getCurrentlyDragged() {
		return currentlyDragged;
	}
    	
    

}
