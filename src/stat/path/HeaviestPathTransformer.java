package stat.path;

import model.graph.Graph;
import stat.IStatTransformer;
import stat.util.AdjacencyList;

public class HeaviestPathTransformer implements IStatTransformer<PathData>{
	
	/**
	 * ALGORITHM SKETCH:

	function Dijkstra(Graph, source):
	
		for each vertex 'v' in Graph:
			let the 'dist[] of v' = infinity
			let the 'prev[] of v' = null
		let the 'distance to the source' = 0
	
	    let 'Q' = the min-heap of all vertices in the graph
	    while 'Q' is not empty:
	    	let 'u' = the vertex in 'Q' with the smallest value in 'dist[]'
	            remove 'u' from 'Q'
	          	for each neighbor 'v' of 'u' if 'v' in 'Q':
					let alt = the 'dist[] of u' + distance(u,v)
					if alt is less than the 'dist[] of v':
						let dist[v] = alt
						let prev[v] = u
	                 	re-insert 'v' in 'Q'
		return dist;
	*/
	
	@Override
	public PathData transform(Graph g, String... args) {
		
		AdjacencyList adj = g.adaptTo(AdjacencyList.class);
		
		return null;
	}

}
