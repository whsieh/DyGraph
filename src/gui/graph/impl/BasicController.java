package gui.graph.impl;

import gui.graph.GController;
import java.awt.event.MouseWheelEvent;

public class BasicController extends GController{
    
    
    public BasicController() {
        super();
    }

    @Override
    public void run() {
        
    }
    
    @Override
    public void handleMouseWheelMoved(MouseWheelEvent e){
        // do nothing
    }

    @Override
    public void launch() {
        this.setPopulator(new RadialClusterPopulator(view,25,1));
        view.activate();
        //if (gpop != null) {
        //    gpop.populate();
        //}
    }

    public static void main(String[] args) {
        BasicController c = new BasicController();
        c.launch();
        //c.view.testTree(10, 4);
    }
    
    
}
