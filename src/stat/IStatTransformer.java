package stat;

import model.graph.Graph;

public interface IStatTransformer<T extends IStatResult>{
	
	public T transform(Graph g,String...args);
	
}
