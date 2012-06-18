package model.graph;

import model.graph.exception.GraphException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Vertex extends GraphItem implements Iterable<Edge>{

    Map<Vertex,Edge> edges;
    
    protected Vertex(String name,Graph myGraph){
        super(name,myGraph);
        edges = new ConcurrentHashMap<Vertex,Edge>();
    }
    
    protected void addEdge(String name,Vertex v,Edge e) {
        if (edges.get(v) == null) {
            edges.put(v,e);
        } else {
            throw GraphException.duplicate();
        }
    }
    
    protected void disconnect() {
        for (Edge e : edges.values()) {
            myGraph.removeEdge(e.name);
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
            return "Vertex: " + name;
        } else if (style == GraphItem.NONVERBOSE){
            return "V<" + name + ">";
        } else {
            throw GraphException.identifier();
        }
    }

    @Override
    public Iterator<Edge> iterator() {
        return edges.values().iterator();
    }
    
}
