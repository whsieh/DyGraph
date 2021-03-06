package gui.graph.example;

import gui.graph.GraphController;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JApplet;

import dygraph.DygraphPopulator;
import dygraph.DygraphViewer;

import stat.comm.CommunityTransformer;

@Deprecated
public class BasicController extends GraphController{
    
    
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
        this.setPopulator(new DygraphPopulator((DygraphViewer)view));
        view.activate();
        /*
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.flush();
        System.out.println("Starting community algorithm...");
        System.out.println(new CommunityTransformer().transform(this.graph));
        */
    }
}
