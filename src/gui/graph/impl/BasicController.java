package gui.graph.impl;

import gui.graph.GController;
import java.awt.event.MouseWheelEvent;
import javax.swing.JApplet;

public class BasicController extends GController{
    
    
    public BasicController(JApplet root) {
        super(root);
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
        this.setPopulator(new RadialClusterPopulator(view,20,2));
        view.activate();
        //if (gpop != null) {
        //    gpop.populate();
        //}
    }    
    
}
