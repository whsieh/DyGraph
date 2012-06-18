
package gui.graph;

abstract public class GPopulator implements Runnable{
    
    protected GViewer view;
    
    public GPopulator(GViewer view) {
        this.view = view;
    }
    
    abstract public void populate();
    
    @Override
    public void run() {
        populate();
    }
    
}
