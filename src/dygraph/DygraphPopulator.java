package dygraph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import model.graph.Graph;
import model.graph.Vertex;

import com.restfb.types.Post;

import dygraph.FacebookGraphData.FacebookVertexData;

import gui.graph.GraphData;
import gui.graph.GraphPopulator;
import gui.graph.GraphViewer;

public class DygraphPopulator extends GraphPopulator {

	final static int SEARCH_DEPTH = 2;
	final static int SEARCH_VOLUME = 80;
	final Set<String> mentioned = new HashSet<String>(200);
	
	public DygraphPopulator(DygraphViewer view) {
		super(view);
	}

	private void iterateFacebookData(ProfileQueryEngine engine, Queue<String> queue) {
		for (Post post : engine.fetchNextPosts()) {
			FacebookGraphData data = FacebookUtil.toGraphData(post);
			view.addGraphData((GraphData)data);
			for (FacebookVertexData vd : data.getVertexData()) {
				queue.add(vd.id);
			}
		}
	}
	
	@Override
	public void populate() {
		
		DygraphViewer fbView = (DygraphViewer)view;
		
		Queue<String> idQueue = new LinkedList<String>();
		
		idQueue.add(ProfileQueryEngine.CURRENT_USER.key());
		int queries = 1;
		
		while (queries < SEARCH_VOLUME && !idQueue.isEmpty()) {
			
			String id = idQueue.remove();

			if (mentioned.contains(id)) {
				continue;
			} else {
				mentioned.add(id);
				ProfileQueryEngine engine = fbView.getProfile(id);
				if (DyGraphConsole.exists()) {
					DyGraphConsole.getInstance().log("Currently on query No. " + queries + ": " + engine.user.getName());
				}
				FacebookVertexPainter vp = fbView.getFacebookProfilePainter(id);
				if (vp != null) {
					vp.setLoading(true);
				}
				for (int i = 0; i < SEARCH_DEPTH; i++) {
					iterateFacebookData(engine, idQueue);
					queries++;
				}
				if (vp != null) {
					vp.setLoading(false);
				}
			}
		}
		Graph g = fbView.getGraph();
		int deltaCount = 0;
		do {
			deltaCount = 0;
			for (String id : g.vertices()) {
				Vertex v = g.findVertex(id);
				if (v.degree() <= 1.0) {
					deltaCount++;
					fbView.removeVertex(id);
				}
			}
		} while(deltaCount > 0);
	}

	
	
}
