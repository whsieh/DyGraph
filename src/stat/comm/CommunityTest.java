package stat.comm;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import stat.util.AdjacencyList;

import Jama.*;

public class CommunityTest {

	public static void main(String[] args) {
		
		long start = System.nanoTime();
		AdjacencyList al = createTestGraph();
		Matrix B = computeModMatrix(al);
		EigenvalueDecomposition decomp = B.eig();
		double[] eigenvalues = decomp.getRealEigenvalues();
		double[][] eigenvectors = decomp.getV().transpose().getArray();
		double greatest = 0.001;
		int indexOfGreatest = -1;
		for (int i = 0; i < eigenvalues.length; i++) {
			if (eigenvalues[i] > greatest) {
				indexOfGreatest = i;
				greatest = eigenvalues[i];
			}
		}
		if (indexOfGreatest != -1) {
			// System.out.println("Graph successfully partitioned with lambda = " + eigenvalues[indexOfGreatest]);
			double[] u = eigenvectors[indexOfGreatest];
			Set<Integer> c1 = new HashSet<Integer>(), c2 = new HashSet<Integer>(), c3 = new HashSet<Integer>();
			for (int i = 0; i < u.length; i++) {
				// System.out.println("Vertex #" + i + " has an eigenvector index: " + u[i]);
				if (u[i] > 0.1) {
					c1.add(i);
				} else if (u[i] < -0.1){
					c2.add(i);
				} else {
					c3.add(i);
				}
			}
			System.out.println("Community #1 consists of: " + c1);
			System.out.println("Community #2 consists of: " + c2);
			System.out.println("The lonely nodes consist of: " + c3 + "     so sad... :(");
		} else {
			System.out.println("No partition found...indivisible graph reached.");
		}
		System.out.println("Total time taken: " + (System.nanoTime() - start)/1000000.0 + " ms");
	}
	
	private static Matrix computeModMatrix(AdjacencyList al) {
		Matrix B = new Matrix(al.getVertexCount(),al.getVertexCount());
		for (int i = 0; i < al.getVertexCount(); i++) {
			for (int c = 0; c <= i; c++ ) {
				double mod = al.weightOf(i,c)-((double)al.getDegree(i)*(double)al.getDegree(c))/(2.0*al.getEdgeCount());
				B.set(i, c, mod);
				B.set(c, i, mod);
			}
		}
		return B;
	}

	private static AdjacencyList createTestGraph() {
		AdjacencyList al = new AdjacencyList(10);
		al.addEdges(new int[][] {
				{0,1},
				{0,2},
				{0,3},
				{1,2},
				{1,3},
				{2,3},
				{3,4},
				{3,8},
				{4,5},
				{4,6},
				{4,8},
				{5,6},
				{5,8},
				{5,9},
				{7,8},
				{7,9},
				{8,9},
		});
		return al;
	}
}
