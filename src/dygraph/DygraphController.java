package dygraph;

import gui.graph.GraphController;
import gui.graph.GraphViewer;

import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;

import dygraph.DygraphPopulator;

import stat.comm.CommunityTransformer;

public class DygraphController extends GraphController<DygraphViewer> {
    
	private DygraphApplet applet;
    
    public DygraphController(JApplet root, DygraphApplet applet) {
        super(root);
        this.applet = applet;
    }
    
    @Override
    public boolean newEdgeEnabled() {
    	return false;
    }
    
    @Override
    public void handleMouseWheelMoved(MouseWheelEvent e) {
    	repulsiveConstant -= e.getWheelRotation()*500;
    	if (repulsiveConstant < 0) {
    		repulsiveConstant = 0;
    	}
    }
    
    @Override
    public void handleKeyPressed(KeyEvent e) {
        super.handleKeyPressed(e);
        view.dragView(e);
    }
    
    public void popURL(String url) {
    	try {
			applet.popURL(new URL(url));
		} catch (MalformedURLException e) {
			DyGraphConsole.tryErr("Failed to popup new URL (" + url + ")");
		}
    }

    @Override
    protected void createViewer() {
    	view = new DygraphViewer(this);
    }
    
    @Override
    public void launch() {
        this.setPopulator(new DygraphPopulator(view));
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