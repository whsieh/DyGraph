package stat.util;

import java.util.Map;

public abstract class IndexIDConverter {

	protected Map<Integer,String> vertexIDMap;
	protected Map<String,Integer> vertexNumberMap;
	
	public void addPair(int i, String id) {
		vertexIDMap.put(i,id);
		vertexNumberMap.put(id,i);
	}
	
	public void addPair(String id, int i) {
		addPair(i,id);
	}
	
	public String getID(int i) {
		return vertexIDMap.get(i);
	}
	
	public Integer getIndex(String id) {
		return vertexNumberMap.get(id);
	}
	
}
