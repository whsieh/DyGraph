package model.graph;



import model.graph.exception.GraphException;

public class Edge extends GraphItem {
    
    public static final int DIRECTED = 0;
    public static final int UNDIRECTED = 1;
    public static final String[] TYPES = {"Undirected","Directed"};
    
    Vertex v1;
    Vertex v2;
    int type;
    double weight;
    
    protected Edge(String name,Vertex v1, Vertex v2,int type,Graph myGraph){
        this(name,v1,v2,type,myGraph,1.0);
    }
    
    protected Edge(String name,Vertex v1, Vertex v2,int type,Graph myGraph,double weight){
        super(name,myGraph);
        this.v1 = v1;
        this.v2 = v2;
        this.type = type;
        this.weight = weight;
        updateVertices();
    }
    
    private void updateVertices() {
        v1.addEdge(v2, this);
        v2.addEdge(v1, this);
    }
    
    /**
     * @return this Edge's type
     */
    public int getType(){
        return type;
    }
    
    public void setWeight(float weight) {
    	this.weight = weight;
    }
    
    public void addWeight(float weight) {
    	this.weight += weight;
    }
    
    /**
     * @return whether this Edge is directed.
     */
    public boolean isDirected(){
        return this.type == Edge.DIRECTED;
    }
    
    /**
     * @return whether this Edge is undirected.
     */
    public boolean isUndirected(){
        return this.type == Edge.UNDIRECTED;
    }

    @Override
    public String toString(int style) {
        switch(style){
            case GraphItem.NONVERBOSE:
                return "E< " + v1.toString() + " , " + v2.toString() + " >";
            case GraphItem.VERBOSE:
                return TYPES[type] + " edge from " + v1.toString() + " to " + v2.toString();
            default:
                throw GraphException.identifier();
        }
    }
    
    @Override
    public int hashCode(){
        return v1.hashCode() + v2.hashCode() + type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Edge other = (Edge) obj;
        if (this.v1 != other.v1 && (this.v1 == null || !this.v1.equals(other.v1))) {
            return false;
        }
        if (this.v2 != other.v2 && (this.v2 == null || !this.v2.equals(other.v2))) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }
    
    
}
