package dygraph;

import gui.graph.AbstractPainter;
import gui.graph.GraphController;
import gui.graph.VertexPainter;
import gui.graph.util.Message;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import util.misc.ImageUtil;

public class DygraphController extends GraphController<DygraphViewer> {
    
	DygraphViewer dView;
	DygraphApplet dRoot;
	Mode mode;
	
	final JFileChooser fileChooser = new JFileChooser();
	boolean isAutoScrolling;
	
	public static enum Mode {
		DEFAULT,
		SEARCH,
		DRAG_DROP,
		COMPARE
	}
	
    public DygraphController(DygraphApplet root) {
        super(root);
        mode = Mode.DEFAULT;
        dRoot = (DygraphApplet)root;
        dView = (DygraphViewer)view;
        isAutoScrolling = true;
    }
    
    protected Mode getMode() {
    	return mode==null ? Mode.DEFAULT : mode;
    }
    
    @Override
    public boolean newEdgeEnabled() {
    	return false;
    }
    
    @Override
    public void handleMouseMoved(MouseEvent e) {
	    if (mode == Mode.COMPARE) {
	    	return;
	    } else {
	    	super.handleMouseMoved(e);
	    }
    }
    
    @Override
    public void handleMouseDragged(MouseEvent e) {
	    if (mode == Mode.COMPARE) {
	    	return;
	    } else if (mode != Mode.DRAG_DROP) {
    		super.handleMouseDragged(e);
    	} else {
    		dView.setCurPosition(e.getX(), e.getY());
    		dView.setDraggingView(false);
    	}
    }
    
    @Override
    public void handleMouseReleased(MouseEvent e) {
	    if (mode == Mode.COMPARE) {
	    	return;
	    } else if (mode == Mode.DRAG_DROP) {
    		VertexPainter vp = view.locateVertexPainter(e.getX(),e.getY());
    		if (vp == null || vp.getID().equals(dView.currentlyGhosted.getID())) {
    			mode = Mode.DEFAULT;
    			dView.currentlyGhosted = null;
    		} else {
    			mode = Mode.COMPARE;
    			dView.openFavoritesPanel(dView.currentlyGhosted.getDisplayName(),
    					((FacebookVertexPainter)vp).getDisplayName());
    			dView.fadeOutViewerBackground();
    			dView.currentlyGhosted = null;
    		}
    	}
    	super.handleMouseReleased(e);
    }
    
    @Override
    public void handleMouseWheelMoved(MouseWheelEvent e) {
	    if (mode == Mode.COMPARE) {
	    	return;
	    } else {
	    	repulsiveConstant -= e.getWheelRotation()*500;
	    	if (repulsiveConstant < 0) {
	    		repulsiveConstant = 0;
	    	}
	    }
    }
    
    @Override
    public void handleMousePressed(MouseEvent e) {
	    if (mode == Mode.COMPARE && dView.favoritesFrame != null) {
	    	Point p = dView.favoritesFrame.getLocation();
    		if (!dView.favoritesFrame.contains(e.getX()-p.x,e.getY()-p.y)) {
    			mode = Mode.DEFAULT;
    			dView.closeFavoritesPanel();
    			dView.fadeInViewerBackground();
    		}
	    } else {
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
    }
    
    @Override
    public void handleKeyReleased(KeyEvent e) {
	    if (mode == Mode.COMPARE) {
	    	return;
	    } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
    		mode = Mode.DEFAULT;
    	}
    }
    
    @Override
    public void handleKeyPressed(KeyEvent e) {
	    if (mode == Mode.COMPARE) {
	    	return;
	    } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
    		if (mode == Mode.DEFAULT) {
    			mode = Mode.SEARCH;
    		}
    	} else if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
    		
    		int returnVal = fileChooser.showOpenDialog(view);
    		if (returnVal == JFileChooser.APPROVE_OPTION) {
    			
    			File file = fileChooser.getSelectedFile();
    			
		        try {
					ImageIO.write(ImageUtil.createImage(dView), "png", file);
				} catch (IOException e1) {
					System.err.println("Error: failed to save " + file.getAbsolutePath());
				}
                //view.getGraphics();
                //ImageIO.write(, "png", outputfile);
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
    }
}