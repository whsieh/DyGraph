package dygraph;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.restfb.types.Post;

import dygraph.FacebookGraphData.FacebookEdgeData;
import dygraph.FacebookGraphData.FacebookVertexData;
import model.graph.Edge;
import gui.graph.EdgePainter;
import gui.graph.GraphController;
import gui.graph.GraphData;
import gui.graph.GraphViewer;
import gui.graph.VertexPainter;
import gui.graph.GraphData.IEdgeData;
import gui.graph.GraphData.IVertexData;
import gui.graph.util.IDCounter;

public class DygraphViewer extends GraphViewer {

	Map<String,ProfileQueryEngine> profileSet;
	
	public DygraphViewer(GraphController controller) {
		super(controller);
		profileSet = new HashMap<String,ProfileQueryEngine>();
	}
	
	@Override
	public void addGraphData(GraphData<? extends IVertexData,? extends IEdgeData> data) {
		if (data instanceof FacebookGraphData) {
			addFacebookGraphData((FacebookGraphData)data);
		} else {
			super.addGraphData(data);
		}
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
		
        VERTEX_POPUPMENU = new JPopupMenu();
        EDGE_POPUPMENU = new JPopupMenu();
        
        VERTEX_MENUITEMS = new JMenuItem[] {new JMenuItem("Visit profile"),
        		new JMenuItem("Expand connections")};
        EDGE_MENUITEMS = new JMenuItem[] {new JMenuItem("See friendship")};
        
        for(int i = 0; i < VERTEX_MENUITEMS.length; i++) {
            VERTEX_POPUPMENU.add(VERTEX_MENUITEMS[i]);
        }
        for(int i = 0; i < EDGE_MENUITEMS.length; i++) {
            EDGE_POPUPMENU.add(EDGE_MENUITEMS[i]);
        }
        this.setBackground(Color.WHITE);
        
        VERTEX_MENUITEMS[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FacebookVertexPainter fbVertex = ((FacebookVertexPainter)currentlySelected);
                ((DygraphController)controller).popURL("http://www.facebook.com/" + fbVertex.getID());
            }
        });
        VERTEX_MENUITEMS[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FacebookVertexPainter fbVertex = ((FacebookVertexPainter)currentlySelected);
                if (fbVertex != null) {
                	expandProfileConnections(fbVertex.getID());
                }
            }
        });
        EDGE_MENUITEMS[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	VertexPainter[] vertices = ((EdgePainter)currentlySelected).getConnectingVertices();
            	FacebookVertexPainter v0 = (FacebookVertexPainter)vertices[0];
            	FacebookVertexPainter v1 = (FacebookVertexPainter)vertices[1];
            	((DygraphController)controller).popURL("http://www.facebook.com/" +
            			getProfile(v0.getID()).user.getUsername() + "?and=" + v1.getID());
            }
        });
	}
	
	protected FacebookVertexPainter getFacebookProfilePainter(String id) {
		return (FacebookVertexPainter)vertexPainterMap.get(id);
	}
	
	@Override
	protected VertexPainter createVertexPainter(int x, int y, String id, String displayName) {
		return new FacebookVertexPainter(this,x,y,id,displayName);
	}
	
	public void expandProfileConnections(final String id) {
		new Thread(new Runnable(){
			@Override
			public void run() {
				ProfileQueryEngine engine = getProfile(id);
				if (engine != null) {
					FacebookVertexPainter fbvp = (FacebookVertexPainter)vertexPainterMap.get(id);
					fbvp.setLoading(true);
					for (Post post : engine.fetchNextPosts()) {
						FacebookGraphData data = FacebookUtil.toGraphData(post);
						addFacebookGraphData(data);
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
    			if (DyGraphConsole.exists()) {
    				DyGraphConsole.getInstance().log("Adding user " + vid + " (" + vd.getName() + ")");
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
    			Edge e = graph.findEdge(eid);
	    		if (e == null) {
	    			if (DyGraphConsole.exists()) {
	    				DyGraphConsole.getInstance().log("Adding connection between " + vid[0] + " and " + vid[1] +
	    						"\n    Message ID: " + ed.getMessageID());
	    			}
	    			addEdge(eid,vp1,vp2,ed.weight());
	    		} else {
	    			e.addWeight(ed.weight());
	    		}
    		}
    	}
    }
	

}
