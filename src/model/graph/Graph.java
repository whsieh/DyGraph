package model.graph;

import model.graph.exception.GraphException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class Graph implements Named,Iterable<Vertex>{
    
    public final static int VERTEX = 0;
    public final static int EDGE = 1;
    
    
    protected Map <String, Edge> edgeMap;
    protected Map <String, Vertex> vertexMap;
    
    protected String name;
    
    public Graph(String name) {
        edgeMap = new LinkedHashMap<String,Edge>();
        vertexMap = new LinkedHashMap<String,Vertex>();
        this.name = name;
    }
    
    public Edge findEdge(String name) {
        return edgeMap.get(name);
    }
    
    public Vertex findVertex(String name) {
        return vertexMap.get(name);
    }
    
    public GraphItem find(String name) {
        GraphItem v = findVertex(name);
        if (v != null) {
            return v;
        } else {
            return findEdge(name);
        }
    }
    
    protected boolean updateVertexKey(String prevName, String newName) {
        Vertex v = vertexMap.remove(prevName);
        if (v != null) {
            vertexMap.put(newName,v);
            v.setName(newName);
            return true;
        } else {
            return false;
        }
    }
    
    protected boolean updateEdgeKey(String prevName,String newName) {
        Edge e = edgeMap.remove(prevName);
        if (e != null) {
            edgeMap.put(newName,e);
            e.setName(newName);
            return true;
        } else {
            return false;
        }
    }
    
    public Edge addEdge(String edgeName,String v1Name,String v2Name,int type) {
        if (findEdge(edgeName) == null || !v1Name.equals(v2Name)) {
            Vertex v1 = findVertex(v1Name);
            Vertex v2 = findVertex(v2Name);
            if (v1 != null && v2 != null) {
                Edge e = new Edge(edgeName,v1,v2,type,this);
                edgeMap.put(edgeName,e);
                return e;
            } else {
                throw GraphException.notFound(" vertex.");
            }
        } else {
            throw GraphException.duplicate();
        }
    }
    
    public Vertex addVertex(String vName) {
        if (findVertex(vName) == null) {
            Vertex v = new Vertex(vName,this);
            vertexMap.put(vName, v);
            return v;
        } else {
            throw GraphException.duplicate();
        }
    }
    
    public Edge removeEdge(String eName) {
        Edge e = findEdge(eName);
        if (e != null) {
            e.v1.removeEdge(e.v2);
            e.v2.removeEdge(e.v1);
            edgeMap.remove(eName);
            return e;
        } else {
            return null;
        }
    }
    
    public Vertex removeVertex(String vName) {
        Vertex v = findVertex(vName);
        if (v != null) {
            v.disconnect();
            vertexMap.remove(vName);
            return v;
        } else {
            return null;
        }
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Iterator<Vertex> iterator() {
        return vertexMap.values().iterator();
    }
    
    @Override
    public String toString() {
        String s = name + ":\n";
        for (Vertex v : vertexMap.values()) {
            s += v + "\n";
            for (Edge e : v.edges.values()) {
                s += "\t" + e + "\n";
            }
        }
        return s;
    }
    
    public static void main(String[] args) {
        Graph g = new Graph("New Graph");
        g.addVertex("Test1");
        g.addVertex("Test2");
        g.addEdge("Edge0","Test1","Test2",Edge.UNDIRECTED);
        g.addVertex("Test1");
        System.out.println(g);
    }
    
}
