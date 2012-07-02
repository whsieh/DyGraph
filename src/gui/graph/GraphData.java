package gui.graph;

import java.util.LinkedList;
import java.util.List;

abstract public class GraphData {

	protected List<IVertexData> vertexInfo;
	protected List<IEdgeData> edgeInfo;
	
	public GraphData() {
		
		vertexInfo = new LinkedList<IVertexData>();
		edgeInfo = new LinkedList<IEdgeData>();
	}
	
	abstract public void addVertexData(List<String> args);
	
	abstract public void addEdgeData(List<String> args);

	
	protected interface IVertexData {
		
		String getID();
	}
	
	
	protected interface IEdgeData {
		
		String getID();
		String[] getVertexID();
	}
}
