package stat.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class BasicIntegerGraph extends IndexIDConverter implements Adaptable {
	
	protected int vertexCount;
	protected int edgeCount;
	
	public BasicIntegerGraph(int vertexCount) {
		
		this.vertexCount = vertexCount;
		this.edgeCount = 0;
		this.vertexIDMap = new HashMap<Integer,String>();
		this.vertexNumberMap = new HashMap<String,Integer>();
		for (int i = 0; i < vertexCount; i++) {
			String id = "#" + i;
			vertexIDMap.put(i,id);
			vertexNumberMap.put(id,i);
		}
	}
	
	public BasicIntegerGraph(Map<String,Integer> vertexNumberMap) {
		
		this.vertexCount = vertexNumberMap.size();
		this.edgeCount = 0;
		this.vertexIDMap = new HashMap<Integer,String>();
		this.vertexNumberMap = vertexNumberMap;
		for (Entry<String,Integer> e : vertexNumberMap.entrySet()) {
			vertexIDMap.put(e.getValue(),e.getKey());
		}
	}
	
	public BasicIntegerGraph(Collection<String> vertexIDs) {
		
		this.vertexCount = vertexIDs.size();
		this.edgeCount = 0;
		this.vertexIDMap = new HashMap<Integer,String>();
		this.vertexNumberMap = new HashMap<String,Integer>();
		int i = 0;
		for (String id : vertexIDs) {
			vertexIDMap.put(i,id);
			vertexNumberMap.put(id,i);
			i++;
		}
	}
	
	/* The default weight of an edge is 1.0 */
	abstract public void addEdge(int v1, int v2);
	
	abstract public void addEdge(int v1, int v2, double weight);
	
	abstract public double weightOf(int v1, int v2);
	
	public void addEdges(int[][] edgeList) {
		try {
			for (int[] edgePair : edgeList) {
				if (edgePair.length > 2) {
					addEdge(edgePair[0],edgePair[1],edgePair[2]);
				} else {
					addEdge(edgePair[0],edgePair[1]);
				}
			}
		} catch (IndexOutOfBoundsException i) {
			System.err.println("Error: no self-edges allowed in this graph.");
		}
	}

	
	public int getVertexCount() {
		return vertexCount;
	}
	
	public int getEdgeCount() {
		return edgeCount;
	}
	
	abstract public <T> T adaptTo(Class cls);
	
}
