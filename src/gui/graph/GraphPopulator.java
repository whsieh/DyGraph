
package gui.graph;

abstract public class GraphPopulator implements Runnable{
    
    protected GraphViewer view;
    
    public GraphPopulator(GraphViewer view) {
        this.view = view;
    }
    
    abstract public void populate();
    
    @Override
    public void run() {
        populate();
    }
    
}
