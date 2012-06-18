
package gui.graph.util;

public class Data <S,I> {

    S source;
    I info;

    public Data(S source, I info){
        this.source = source;
        this.info = info;
    }

    public Data(I info){
        this.source = null;
        this.info = info;
    }
    
    public S source() {
        return source;
    }
    
    public I info() {
        return info;
    }
    
}