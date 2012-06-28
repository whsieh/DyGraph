package gui.graph.impl;

import gui.graph.GController;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JApplet;

public class BasicController extends GController{
    
    
    public BasicController(JApplet root) {
        super(root);
    }
    
    @Override
    public void handleMouseWheelMoved(MouseWheelEvent e){
        // do nothing
    }
    
    @Override
    public void handleKeyPressed(KeyEvent e) {
        super.handleKeyPressed(e);
        view.dragView(e);
    }

    @Override
    public void launch() {
        this.setPopulator(new RadialClusterPopulator(view,50,4));
        view.activate();
    }
}
