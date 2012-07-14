package dygraph;

import gui.graph.GraphController;
import gui.graph.GraphViewer;

import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.JApplet;

import dygraph.FacebookGraphPopulator;

import stat.comm.CommunityTransformer;

public class FacebookGraphController extends GraphController<FacebookGraphViewer>{
    
    
    public FacebookGraphController(JApplet root) {
        super(root);
    }
    
    @Override
    public void handleKeyPressed(KeyEvent e) {
        super.handleKeyPressed(e);
        view.dragView(e);
    }

    @Override
    protected void createViewer() {
    	view = new FacebookGraphViewer(this);
    }
    
    @Override
    public void launch() {
        this.setPopulator(new FacebookGraphPopulator(view));
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