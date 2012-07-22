package gui.graph;

import java.util.LinkedList;
import java.util.List;

abstract public class GraphData <V extends GraphData.IVertexData, E extends GraphData.IEdgeData> {

	private List<V> vertexInfo;
	private List<E> edgeInfo;
	
	public GraphData() {
		
		setVertexInfo(new LinkedList<V>());
		setEdgeInfo(new LinkedList<E>());
	}
	
	abstract public void addVertexData(List<String> args);
	
	abstract public void addEdgeData(List<String> args);

	
	public List<V> getVertexInfo() {
		return vertexInfo;
	}

	public void setVertexInfo(List<V> vertexInfo) {
		this.vertexInfo = vertexInfo;
	}


	public List<E> getEdgeInfo() {
		return edgeInfo;
	}

	public void setEdgeInfo(List<E> edgeInfo) {
		this.edgeInfo = edgeInfo;
	}


	public interface IVertexData {

		String getID();
	}
	
	
	public interface IEdgeData {
		
		String getID();
		String[] getVertexID();
		float weight();
	}
}
