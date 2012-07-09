package stat.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class AdjacencyList extends BasicIntegerGraph{
	
	protected final static int PRIME_1 = 47533;
	protected final static int PRIME_2 = 571;
	
	private LinkedList<Integer>[] adj;
	private Map<Integer, Double> weights;
	private Double[] degrees;
	
	public AdjacencyList(int vertexCount) {
		super(vertexCount);
		this.adj = (LinkedList<Integer>[])new LinkedList[vertexCount];
		this.weights = new HashMap<Integer,Double>();
		this.degrees = new Double[vertexCount];
		for (int i = 0; i < vertexCount; i++) {
			adj[i] = new LinkedList<Integer>();
			degrees[i] = 0.0;
		}
	}
	
	public AdjacencyList(Map<String,Integer> vertexNumberMap) {
		super(vertexNumberMap);
		this.adj = (LinkedList<Integer>[])new LinkedList[vertexNumberMap.size()];
		this.weights = new HashMap<Integer,Double>();
		this.degrees = new Double[vertexCount];
		for (int i = 0; i < vertexCount; i++) {
			adj[i] = new LinkedList<Integer>();
			degrees[i] = 0.0;
		}
	}
	
	public Double getDegree(int v1) {
		return degrees[v1];
	}
	
	@Override
	public void addEdge(int v1, int v2) {
		adj[v1].add(v2);
		adj[v2].add(v1);
		weights.put(hash(v1,v2), 1.0);
		degrees[v1] += 1.0;
		degrees[v2] += 1.0;
		edgeCount++;
	}
	
	@Override
	public void addEdge(int v1, int v2, double weight) {
		adj[v1].add(v2);
		adj[v2].add(v1);
		weights.put(hash(v1,v2), weight);
		degrees[v1] += weight;
		degrees[v2] += weight;
		edgeCount++;
	}
	
	@Override
	public double weightOf(int v1, int v2) {
		Double w = weights.get(hash(v1,v2));
		if (w == null) {
			return 0;
		} else {
			return w;
		}
	}
	
	public String toString() {
		
		StringBuilder s = new StringBuilder(vertexCount + " vertices; " + edgeCount + " edges\n\n");
		for (int i = 0; i < vertexCount; i++) {
			s.append("(" + i + "): " + adj[i] + "\n");
		}
		s.append("\nLabels: " + vertexIDMap + " and\n\t" + vertexNumberMap);
		return s.toString();
	}

	/* One way storage of edge weights. Cannot use hashed value to 
	 * determine corresponding edges without lengthy computation. */
	static int hash(int v1, int v2) {
		
		if (v1 < v2) {
			return (PRIME_1*v1 + PRIME_2*v2);
		} else {
			return (PRIME_1*v2 + PRIME_2*v1);
		}
	}

	@Override
	public <T> T adaptTo(Class cls) {
		
		if (cls == AdjacencyMatrix.class) {
			AdjacencyMatrix am = new AdjacencyMatrix(vertexCount);
			for (int v1 = 0; v1 < vertexCount; v1++) {
				for (Integer v2 : adj[v1]) {
					if (v1 <= v2) {
						double weight = weightOf(v1,v2);
						am.addEdge(v1,v2,weight);
						am.addEdge(v2,v1,weight);
					}
				}
			}
			return (T)am;
		} else {
			return null;
		}
	}
	
	
}
