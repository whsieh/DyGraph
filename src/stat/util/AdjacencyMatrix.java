package stat.util;

import Jama.*;

public class AdjacencyMatrix extends BasicIntegerGraph{

	private Matrix matrix;
	
	public AdjacencyMatrix(int vertexCount) {
		super(vertexCount);
		this.matrix = new Matrix(vertexCount,vertexCount);
	}

	@Override
	public <T> T adaptTo(Class cls) {
		/* TODO Add an adapter for AdjacencyList */
		return null;
	}

	@Override
	public void addEdge(int v1, int v2) {
		matrix.set(v1, v2, 1.0);
		edgeCount++;
	}

	@Override
	public void addEdge(int v1, int v2, double weight) {
		matrix.set(v1, v2, weight);
		edgeCount++;
	}

	@Override
	public double weightOf(int v1, int v2) {
		return matrix.get(v1,v2);
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < vertexCount; i++) {
			for (int c = 0; c < vertexCount; c++) {
				s.append(weightOf(i,c));
				s.append(" ");
			}
			s.append("\n");
		}
		
		return s.toString();
	}
	
}
