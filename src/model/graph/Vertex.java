package model.graph;

import model.graph.exception.GraphException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Vertex extends GraphItem implements Iterable<Edge>{

    Map<Vertex,Edge> edges;
    
    protected Vertex(String id,Graph myGraph){
        super(id,myGraph);
        edges = new ConcurrentHashMap<Vertex,Edge>();
    }
    
    protected void disconnect() {
        for (Edge e : edges.values()) {
            myGraph.removeEdge(e.id);
        }
    }    
    
    protected void addEdge(Vertex v,Edge e) {
        if (edges.get(v) == null) {
            edges.put(v,e);
        } else {
            throw GraphException.duplicate();
        }
    }
    
    protected void removeEdge(Vertex v) {
        Edge e = edges.remove(v);
        if (e != null && e.isUndirected()) {
            v.edges.remove(this);
        }
    }
    
    @Override
    public String toString(int style) {
        if (style == GraphItem.VERBOSE) {
            return "Vertex: " + id;
        } else if (style == GraphItem.NONVERBOSE){
            return "V<" + id + ">";
        } else {
            throw GraphException.identifier();
        }
    }

    @Override
    public Iterator<Edge> iterator() {
        return edges.values().iterator();
    }
    
}
