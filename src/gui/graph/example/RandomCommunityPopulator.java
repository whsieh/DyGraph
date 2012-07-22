package gui.graph.example;

import gui.graph.GraphData;
import gui.graph.GraphPopulator;
import gui.graph.GraphViewer;
import dygraph.FacebookGraphData;

@Deprecated
public class RandomCommunityPopulator extends GraphPopulator {

	final static int NUM_COMMUNITIES = 4;
	final static int COMMUNITY_SIZE = 20;
	final static char[] LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	
	public RandomCommunityPopulator(GraphViewer view) {
		super(view);
	}

	private char toChar(int num) {
		return LETTERS[num];
	}
	
	@Override
	public void populate() {
		FacebookGraphData data = new FacebookGraphData();
		String[][] vertices = new String[NUM_COMMUNITIES][COMMUNITY_SIZE];
		for (int comm = 0; comm < NUM_COMMUNITIES; comm++)  {
			for (int index = 0; index < COMMUNITY_SIZE; index++) {
				vertices[comm][index] = "[" + toChar(comm) + ":" + index + "]";
				data.addVertexData(vertices[comm][index],vertices[comm][index]);
			}
		}
		for (int comm1 = 0; comm1 < NUM_COMMUNITIES; comm1++)  {
			for (int index1 = 0; index1 < COMMUNITY_SIZE; index1++) {
				String[] communityVertices = vertices[comm1];
				for (int index2 = index1+1; index2 < COMMUNITY_SIZE; index2++) {
					if (Math.random() < 0.75) {
						data.addEdgeData(communityVertices[index1], communityVertices[index2],
								"testing", 1.0f);
					}
				}
				for (int comm2 = comm1+1; comm2 < NUM_COMMUNITIES; comm2++) {
					for (int index2 = 0; index2 < COMMUNITY_SIZE; index2++) {
						if (Math.random() < 0.01) {
							data.addEdgeData(vertices[comm1][index1], vertices[comm2][index2],
									"testing", 1.0f);
						}
					}
				}
			}
		}
		view.addGraphData((GraphData)data);
	}
	
	

}
