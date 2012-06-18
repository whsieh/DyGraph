package model.graph;

public enum Message {

    ADD_VERTEX,
    REMOVE_VERTEX,
    EDIT_VERTEX,
    QUERY_VERTEX,
    
    ADD_EDGE,
    REMOVE_EDGE,
    EDIT_EDGE,
    QUERY_EDGE,
    
    VERTEX_RENAME,
    EDGE_RENAME
    
}

class Data{
    
    Object source;
    Object info;
    
    public Data(Object source, Object info){
        this.source = source;
        this.info = info;
    }
    
    public Data(Object info){
        this.source = null;
        this.info = info;
    }
    
    
}