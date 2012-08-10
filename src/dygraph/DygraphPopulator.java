package dygraph;

import gui.graph.GraphData;
import gui.graph.GraphPopulator;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import com.restfb.types.Post;

import dygraph.FacebookGraphData.FacebookVertexData;

public class DygraphPopulator extends GraphPopulator {

	final static int SEARCH_DEPTH = 2;
	final static int SEARCH_VOLUME = 25;
	final Set<String> mentioned = new HashSet<String>(200);
	
	public DygraphPopulator(DygraphViewer view) {
		super(view);
	}

	private void iterateFacebookData(ProfileQueryEngine engine, Queue<String> queue) {
		for (Post post : engine.fetchNextPosts()) {
			FacebookGraphData data = FacebookUtil.toGraphData(engine.user,post);
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

			if (!mentioned.contains(id)) {
				mentioned.add(id);
				ProfileQueryEngine engine = fbView.getProfile(id);
				DygraphConsole.tryLog("Currently on query " + queries + "/" + SEARCH_VOLUME + ", " + engine.user.getName());
				for (int i = 0; i < SEARCH_DEPTH; i++) {
					iterateFacebookData(engine, idQueue);
					queries++;
				}
			}
		}
	}

	
	
}
