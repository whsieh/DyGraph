package dygraph;

import gui.graph.EdgePainter;
import gui.graph.GraphController;
import gui.graph.GraphData;
import gui.graph.GraphData.IEdgeData;
import gui.graph.GraphData.IVertexData;
import gui.graph.GraphViewer;
import gui.graph.VertexPainter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import model.graph.Edge;
import model.graph.Vertex;
import util.misc.ImageLibrary;

import com.restfb.types.Post;

import dygraph.FacebookGraphData.FacebookEdgeData;
import dygraph.FacebookGraphData.FacebookVertexData;

public class DygraphViewer extends GraphViewer {

	Map<String,ProfileQueryEngine> profileSet;
	DygraphController dController;
	
	public DygraphViewer(GraphController controller) {
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
	
	@Override
	public void paintFrame(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		super.paintFrame(g2d);
		switch(dController.getMode()) {
			case SEARCH:
				g2d.drawImage(ImageLibrary.grabImage(
						"http://dygraph.herobo.com/img/green_plus.png", true), curX+10, curY+10, this);
				break;
		}
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
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
        
        WHITESPACE_MENUITEMS = new JMenuItem[] {new JMenuItem("Prune connections")};
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
        
        WHITESPACE_MENUITEMS[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	pruneConnections();
            }
        });
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
					fbvp.setLoading(true);
					ProfileQueryEngine engine = getProfile(id);
					if (engine != null) {
						for (int i = 0; i < count; i++) {
							fbvp.setLoading(true);
							for (Post post : engine.fetchNextPosts()) {
								FacebookGraphData data = FacebookUtil.toGraphData(post);
								addFacebookGraphData(data);
							}
							fbvp.setLoading(false);
						}
					}
					fbvp.setLoading(false);
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
    			this.addVertex(vid, randomPoints[0], randomPoints[1],vd.getName());
    		}
    	}
    	
    	for (FacebookEdgeData ed : data.getEdgeInfo()) {
    		String eid = ed.getID();
    		String[] vid = ed.getVertexID();
    		if (graph.findVertex(vid[0]) != null && graph.findVertex(vid[1]) != null) {
    			VertexPainter vp1 = vertexPainterMap.get(vid[0]);
    			VertexPainter vp2 = vertexPainterMap.get(vid[1]);
    			EdgePainter ep = edgePainterMap.get(eid);
    			Edge e = graph.findEdge(eid);
	    		if (e == null && ep == null) {
	    			addEdge(eid,vp1,vp2,ed.weight());
	    		} else if (e != null && ep != null){
	    			e.addWeight(ed.weight());
	    			ep.addWeight(ed.weight());
	    		}
    		}
    	}
    }
	

}
