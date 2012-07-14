package dygraph;

import dygraph.FacebookGraphData.FacebookVertexData;
import model.graph.Edge;
import gui.graph.GraphController;
import gui.graph.GraphData;
import gui.graph.GraphViewer;
import gui.graph.VertexPainter;
import gui.graph.GraphData.IEdgeData;
import gui.graph.GraphData.IVertexData;

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
	
    private void addFacebookGraphData(FacebookGraphData data) {
    	for (FacebookVertexData vd : data.getVertexInfo()) {
    		String vid = vd.getID();
    		if (graph.findVertex(vid) == null) {
    			int[] randomPoints = GraphViewer.getRandomPoints();
    			this.addVertex(vid, randomPoints[0], randomPoints[1],vd.getName());
    		}
    	}
    	for (IEdgeData ed : data.getEdgeInfo()) {
    		String eid = ed.getID();
    		String[] vid = ed.getVertexID();
    		if (graph.findVertex(vid[0]) != null && graph.findVertex(vid[1]) != null) {
    			VertexPainter vp1 = vertexPainterMap.get(vid[0]);
    			VertexPainter vp2 = vertexPainterMap.get(vid[1]);
    			Edge e = graph.findEdge(eid);
	    		if (e == null) {
	    			addEdge(eid,vp1,vp2,ed.weight());
	    		} else {
	    			e.addWeight(ed.weight());
	    		}
    		}
    	}
    }
	

}
