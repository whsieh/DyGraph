package dygraph;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.restfb.types.Post;

import dygraph.FacebookGraphData.FacebookVertexData;

import gui.graph.GraphData;
import gui.graph.GraphPopulator;
import gui.graph.GraphViewer;

public class FacebookGraphPopulator extends GraphPopulator {

	final static int SEARCH_DEPTH = 1;
	final private Map<String,ProfileQueryEngine> profiles;
	
	public FacebookGraphPopulator(GraphViewer view) {
		super(view);
		profiles = new HashMap<String,ProfileQueryEngine>();
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
		
		Queue<String> idQueue = new LinkedList<String>();
		
		idQueue.add(ProfileQueryEngine.CURRENT_USER.key());
		int queries = 1;
		
		while (queries < 10 && !idQueue.isEmpty()) {
			
			String id = idQueue.remove();
			
			ProfileQueryEngine engine;
			if (profiles.containsKey(id)) {
				engine = profiles.get(id);
			} else {
				engine = new ProfileQueryEngine(id);
				profiles.put(id,engine);				
			}
			
			for (int i = 0; i < SEARCH_DEPTH; i++) {
				iterateFacebookData(engine, idQueue);
				queries++;
			}
			
		}
	}

	
	
}
