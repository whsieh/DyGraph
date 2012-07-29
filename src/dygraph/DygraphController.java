package dygraph;

import gui.graph.AbstractPainter;
import gui.graph.GraphController;
import gui.graph.VertexPainter;
import gui.graph.util.Message;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.net.MalformedURLException;
import java.net.URL;

public class DygraphController extends GraphController<DygraphViewer> {
    
	DygraphViewer dView;
	DygraphApplet dRoot;
	Mode mode;
	
	boolean isAutoScrolling;
	
	public static enum Mode {
		DEFAULT,
		SEARCH,
		BROWSE,
		MENU
	}
	
    public DygraphController(DygraphApplet root) {
        super(root);
        dRoot = (DygraphApplet)root;
        dView = (DygraphViewer)view;
        mode = Mode.DEFAULT;
        isAutoScrolling = true;
    }
    
    Mode getMode() {
    	return mode;
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
    public void handleMousePressed(MouseEvent e) {
    	int x = e.getX(), y = e.getY();
    	if (e.isShiftDown() && e.getButton() == 1) {
    		FacebookVertexPainter fvp = dView.locateVertexPainter(x, y);
    		if (fvp != null) {
    			dView.expandProfileConnections(fvp.getID(), 5);
    			AbstractPainter ap = dView.getCurrentlySelected();
    			if (ap != null) {
    				ap.inform(Message.MOUSE_DESELECTED, null);
    			}
    			dView.setCurrentlySelected(fvp);
    			fvp.inform(Message.MOUSE_CLICKED, null);
    		}
    	} else {
    		super.handleMousePressed(e);
		}
    }
    
    @Override
    public void handleKeyReleased(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
    		mode = Mode.DEFAULT;
    	}
    }
    
    @Override
    public void handleKeyPressed(KeyEvent e) {
    	if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
    		if (mode != Mode.MENU) {
    			mode = Mode.SEARCH;
    		}
    	} else {
	        super.handleKeyPressed(e);
	        view.dragView(e);
    	}
    }
    
    public void popURL(String url) {
    	try {
			((DygraphApplet)root).popURL(new URL(url));
		} catch (MalformedURLException e) {
			DygraphConsole.tryErr("Failed to popup new URL (" + url + ")");
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