package model.graph;


import java.util.HashMap;
import java.util.Map;

public abstract class GraphItem implements Named{
    
    final static int VERBOSE = 0;
    final static int NONVERBOSE = 1;
    
    protected Map<String,Object> info;
    protected String id;
    protected Graph myGraph;
    
    GraphItem(String id,Graph myGraph){
        info = new HashMap<String,Object>();
        this.id = id;
        this.myGraph = myGraph;
    }
    
    public Object getInfo(String key) {
        return info.get(key);
    }
    
    @Override
    public String getName() {
        return id;
    }

    void setName(String id) {
        myGraph.updateVertexKey(this.id, id);
        this.id = id;
    }
    
    /**
     * Adds the given data to the graph component under the given key.
     */
    public void addData(String key, Object value){
        info.put(key, value);
    }
    
    /**
     * @return a String representation of this graph element, either in a
     * verbose or non-verbose manner.
     */
    abstract public String toString(int style);
    
    /**
     * @return the non-verbose variant of the toString method
     */
    @Override
    public String toString(){
        return toString(GraphItem.NONVERBOSE);
    }
    
    
    
    
    
    
    
    
    
    
}
