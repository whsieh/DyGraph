
package gui.graph.util;

public class Data {

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
    
    public Object source() {
        return source;
    }
    
    public Object info() {
        return info;
    }
    
}