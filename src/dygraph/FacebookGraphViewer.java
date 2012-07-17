package dygraph;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import dygraph.FacebookGraphData.FacebookEdgeData;
import dygraph.FacebookGraphData.FacebookVertexData;
import model.graph.Edge;
import gui.console.Console;
import gui.graph.EdgePainter;
import gui.graph.GraphController;
import gui.graph.GraphData;
import gui.graph.GraphViewer;
import gui.graph.VertexPainter;
import gui.graph.GraphData.IEdgeData;
import gui.graph.GraphData.IVertexData;
import gui.graph.util.IDCounter;

public class FacebookGraphViewer extends GraphViewer {

	public FacebookGraphViewer(GraphController controller) {
		super(controller);
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
	
	@Override
	protected void createContextMenu() {
		
        WHITESPACE_POPUPMENU = new JPopupMenu();
        VERTEX_POPUPMENU = new JPopupMenu();
        EDGE_POPUPMENU = new JPopupMenu();
        WHITESPACE_MENUITEMS = new JMenuItem[] {new JMenuItem("New vertex")};
        VERTEX_MENUITEMS = new JMenuItem[] {new JMenuItem("Delete vertex"),
            new JMenuItem("Add edge")};
        EDGE_MENUITEMS = new JMenuItem[] {new JMenuItem("Delete edge")};
        
        for(int i = 0; i < WHITESPACE_MENUITEMS.length; i++) {
            WHITESPACE_POPUPMENU.add(WHITESPACE_MENUITEMS[i]);
        }
        for(int i = 0; i < VERTEX_MENUITEMS.length; i++) {
            VERTEX_POPUPMENU.add(VERTEX_MENUITEMS[i]);
        }
        for(int i = 0; i < EDGE_MENUITEMS.length; i++) {
            EDGE_POPUPMENU.add(EDGE_MENUITEMS[i]);
        }
        WHITESPACE_MENUITEMS[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addVertex("#" + IDCounter.next(),popupX,popupY);
            }
        });
        
        this.setBackground(Color.WHITE);
        VERTEX_MENUITEMS[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeVertex((VertexPainter)currentlySelected);
            }
        });
        EDGE_MENUITEMS[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeEdge((EdgePainter)currentlySelected);
            }
        });
        VERTEX_MENUITEMS[1].addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               currentlyAddingEdge = true;
           }
        });
	}
	
	@Override
	protected VertexPainter createVertexPainter(int x, int y, String id, String displayName) {
		return new FacebookVertexPainter(this,x,y,id,displayName);
	}
	
    private void addFacebookGraphData(FacebookGraphData data) {
    	for (FacebookVertexData vd : data.getVertexInfo()) {
    		String vid = vd.getID();
    		if (graph.findVertex(vid) == null) {
    			int[] randomPoints = GraphViewer.getRandomPoints();
    			if (Console.exists()) {
    				Console.getInstance().log("Adding user " + vid + " (" + vd.getName() + ")");
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
	    			if (Console.exists()) {
	    				Console.getInstance().log("Adding connection between " + vid[0] + " and " + vid[1] +
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
