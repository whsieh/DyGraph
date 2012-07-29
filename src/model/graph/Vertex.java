package model.graph;

import model.graph.exception.GraphException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import dygraph.DygraphConsole;

public class Vertex extends GraphItem implements Iterable<Edge>{

    protected Map<Vertex,Edge> edges;
    protected double weight;
    
    protected Vertex(String id,Graph myGraph) {
        super(id,myGraph);
        edges = new ConcurrentHashMap<Vertex,Edge>();
    }
    
    public double weight() {
    	return weight;
    }
    
    public int degree() {
    	return edges.size();
    }
    
    protected void disconnect() {
        for (Edge e : edges.values()) {
            myGraph.removeEdge(e.id);
        }
        weight = 0.0;
    }
    
    protected void addEdge(Vertex v,Edge e) {
        if (edges.get(v) == null) {
            edges.put(v,e);
            weight += e.weight;
        } else {
            throw GraphException.duplicate();
        }
    }
    
    protected void removeEdge(Vertex v) {
        Edge e = edges.remove(v);
        if (e != null && e.isUndirected()) {
        	weight -= e.weight;
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
