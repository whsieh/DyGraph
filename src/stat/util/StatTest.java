package stat.util;

import java.util.Arrays;

import Jama.*;

public class StatTest {

	public static void main(String[] args) {
		
		submatrix();
	}
	
	public static void eigen() {
		double[][] m = new double[][] {
				{-0.2647058823529412, 0.7352941176470589, 0.7352941176470589, 0.5588235294117647},
				{0.7352941176470589, -0.2647058823529412, 0.7352941176470589, 0.5588235294117647},
				{0.7352941176470589, 0.7352941176470589, -0.2647058823529412, 0.5588235294117647},
				{0.5588235294117647, 0.5588235294117647, 0.5588235294117647, -0.7352941176470589},
		};
		Matrix mat = new Matrix(m);
		EigenvalueDecomposition eig = new EigenvalueDecomposition(mat);
		StringBuilder sb = new StringBuilder();
		sb.append(Arrays.toString(eig.getRealEigenvalues()));
		for (double[] u : eig.getV().getArray()) {
			sb.append("\n" + Arrays.toString(u));
		}
		System.out.println(sb.toString());
	}
	
	public static void submatrix() {
		double[][] m = new double[][] {
				{1,2,3,4,5},
				{2,3,4,5,6},
				{3,4,5,6,7},
				{4,5,6,7,8},
				{5,6,7,8,9},
		};
		Matrix mat = new Matrix(m);
		int[] subIndices = new int[]{1,3,4};
		Matrix sub = mat.getMatrix(subIndices,subIndices);
		System.out.println(matrixToString(sub));
	}
	
	private static String matrixToString(Matrix m) {
		StringBuilder s = new StringBuilder();
		for (double[] row : m.getArray()) {
			String str = Arrays.toString(row);
			s.append(str.substring(1,str.length()-1) + "\n");
		}
		return s.toString();
	}
	
}
