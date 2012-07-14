package model.graph;

import model.graph.exception.GraphException;
import stat.util.Adaptable;
import stat.util.AdjacencyList;
import stat.util.BasicIntegerGraph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Graph implements Named,Iterable<Vertex>,Adaptable{
    
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
    
    public boolean hasEdge(String vID1, String vID2) {
    	Vertex v1 = findVertex(vID1);
    	Vertex v2 = findVertex(vID2);
    	if (v1 == null || v2 == null) {
    		return false;
    	} else {
    		return v1.edges.containsKey(v2) || v2.edges.containsKey(v1);
    	}
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
    
    public Set<String> edges() {
    	return edgeMap.keySet();
    }
    
    public Set<String> vertices() {
    	return vertexMap.keySet();
    }
    
    public Edge addEdge(String edgeName,String v1Name,String v2Name,int type, double weight) {
        if (findEdge(edgeName) == null || !v1Name.equals(v2Name)) {
            Vertex v1 = findVertex(v1Name);
            Vertex v2 = findVertex(v2Name);
            if (v1 != null) {
            	if (v2 != null) {
	                Edge e = new Edge(edgeName,v1,v2,type,this, weight);
	                edgeMap.put(edgeName,e);
	                return e;
            	} else {
            		throw GraphException.notFound(" vertex " + v2Name);
            	}
            } else {
                throw GraphException.notFound(" vertex " + v1Name);
            }
        } else {
            throw GraphException.duplicate();
        }
    }
    
    public Edge addEdge(String v1Name,String v2Name,int type) {
        return addEdge(v1Name+"_"+v2Name,v1Name,v2Name,type, 1.0);
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

	@Override
	public <T> T adaptTo(Class cls) {
		
		if (cls == AdjacencyList.class) {
			
			Map<String,Integer> vertexNumberMap = new HashMap<String,Integer>();
			int c = 0;
			for (Vertex v : this) {
				vertexNumberMap.put(v.id,c);
				c++;
			}
			
			AdjacencyList al = new AdjacencyList(vertexNumberMap);
			for (Edge e : edgeMap.values()) {
				int vIndex1 = al.getIndex(e.v1.id);
				int vIndex2 = al.getIndex(e.v2.id);
				if (vIndex1 < vIndex2) {
					al.addEdge(vIndex1, vIndex2, e.weight);
				}
			}
			
			return (T)al;
		}
		return null;
	}
    
}
