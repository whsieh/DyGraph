package gui.graph.example;

import gui.graph.GController;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JApplet;

public class BasicController extends GController{
    
    
    public BasicController(JApplet root) {
        super(root);
    }
    
    @Override
    public void handleKeyPressed(KeyEvent e) {
        super.handleKeyPressed(e);
        view.dragView(e);
    }

    @Override
    public void launch() {
        this.setPopulator(new RadialClusterPopulator(view,25,4));
        view.activate();
    }
}
