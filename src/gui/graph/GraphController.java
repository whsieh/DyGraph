package gui.graph;

import gui.graph.util.Message;
import gui.graph.util.Data;
import gui.graph.util.IDCounter;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JApplet;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import model.graph.Graph;

public abstract class GraphController<T extends GraphViewer> {
    
    protected static final float DEFAULT_REPULSIVE_CONST = (float)Math.pow(2,12);
    protected static final float DEFAULT_EQUILIBRIUM_LENGTH = 100;
    protected static final float DEFAULT_UNIT_MASS = 15000;
    
    protected static final int DEFAULT_WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().width * 0.75);
    protected static final int DEFAULT_HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().height * 0.75);
    protected static final int SCREEN_SIZE_MULT = 4;
    
    static{
    	try{ 
		   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e){}
    }
    
    protected T view;
    protected JApplet root;
    
    protected float repulsiveConstant = DEFAULT_REPULSIVE_CONST;
    protected float equilibriumLength = DEFAULT_EQUILIBRIUM_LENGTH;
    protected float unitMass = DEFAULT_UNIT_MASS;
    
    protected GraphPopulator gpop;
    protected Graph graph;
    
    public GraphController(JApplet root) {
        this.root = root;
        // root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createViewer();
        root.setLayout(new GridLayout());
        root.add(view);
        root.setLocation(DEFAULT_WIDTH/6,DEFAULT_HEIGHT/6);
        root.setVisible(true);
        view.setLayout(null);
        addInputListeners();

        gpop = new GraphPopulator(view){
            @Override
            public void populate() {
                // empty graph
            }
        };
        
        graph = view.graph;
    }
    
    public GraphViewer getView() {
    	return view;
    }
    
    public Graph getModel() {
    	return graph;
    }
    
    protected void createViewer() {
    	view = (T)new GraphViewer(this);
    }
    
    public void setPopulator(GraphPopulator pop) {
        this.gpop = pop;
        new Thread(gpop).start();
    }
    
    private void addInputListeners() {
        addMouseMotion();
        addMouseAction();
        addMouseWheelMotion();
        addKeyboardAction();
    }
    
    private void addMouseMotion() {
        view.addMouseMotionListener(new MouseAdapter(){
            
            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseMoved(e);
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDragged(e);
            }
        });
    }
    private void addMouseAction() {
        view.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e) {
                handleMouseReleased(e);
            }
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouseClicked(e);
            }
        });
    }
    private void addMouseWheelMotion() {
        view.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                handleMouseWheelMoved(e);
            }
        });
    }
    private void addKeyboardAction() {
        view.addKeyListener(new KeyAdapter() {
            
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPressed(e);
            }
        });
    }
    
    public void handleKeyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (view.currentlySelected != null) {
                if (view.currentlySelected instanceof EdgePainter) {
                    view.removeEdge((EdgePainter)view.currentlySelected);
                } else if (view.currentlySelected instanceof VertexPainter) {
                    view.removeVertex((VertexPainter)view.currentlySelected);
                }
                view.currentlySelected = null;
            }
        }
    }
    
    public <T> T getData(String id, String key) {
    	try {
    		return (T)graph.find(id).getData(key);
    	} catch (ClassCastException e){
    		System.err.println("Invalid class type when extracting: " +
    				key + " from " + id);
    		return null;
    	} catch (NullPointerException e) {
    		System.err.println("Invalid identifier: " + id);
    		return null;
    	}
    }
    
    public void setData(String id, String key, Object o) {
    	try {
    		graph.find(id).setData(key,o);
    	} catch (NullPointerException e) {
    		System.err.println("Invalid identifier: " + id);
    	}
    }
    
    public void handleMouseWheelMoved(MouseWheelEvent e) {
        if (e.isControlDown()) {
            repulsiveConstant -= e.getWheelRotation()*20;
        } else if (e.isShiftDown() ) {
            unitMass -= e.getWheelRotation() * 10;
        }else {
            equilibriumLength -= e.getWheelRotation()*10;
        }
    }
    public void handleMouseReleased(MouseEvent e) {
        if (view.currentlyDragged != null) {
            view.currentlyDragged = null;
        }
        if ( view.currentlyFocused != null) {
            view.currentlyFocused.inform(Message.MOUSE_EXITED, new Data(e));
            view.currentlyFocused = null;
        }
        if (view.currentlySelected != null) {
        	if (!(e.getX() == view.curX && e.getY() == view.curY)) {
	        	view.currentlySelected.inform(Message.MOUSE_DESELECTED, new Data(e));
	        	view.currentlySelected = null;
        	}
        }
    }
    public void handleMouseClicked(MouseEvent e) {    
        view.requestFocus();
        VertexPainter node = view.locateVertexPainter(e.getX(),e.getY());
        EdgePainter edge = view.locateEdgePainter(e.getX(),e.getY());
        if (node != null) {
            if (view.currentlyAddingEdge) {
                if (view.currentlyFocused != null) {
                    view.addEdge((VertexPainter)view.currentlyFocused,
                            (VertexPainter)view.currentlySelected);
                    if (e.isShiftDown()) {
                        view.currentlySelected.inform(Message.MOUSE_DESELECTED,new Data(e));
                        view.currentlySelected = view.currentlyFocused;
                        view.currentlyFocused = null;
                        view.currentlySelected.inform(Message.MOUSE_CLICKED, null);
                    } else {
                        view.currentlyAddingEdge = false;
                    }
                } else {
                    view.currentlyAddingEdge = false;
                }
            } else {
                if ( view.currentlySelected != null ) {
                    view.currentlySelected.inform(Message.MOUSE_DESELECTED,new Data(e));
                }
                view.currentlySelected = node;
                node.inform(Message.MOUSE_CLICKED,new Data(e));
                if (e.isShiftDown()  && newEdgeEnabled()) {
                    view.currentlyAddingEdge = true;
                }
            }
        }  else if (edge != null) {
            if ( view.currentlySelected != null ) {
                view.currentlySelected.inform(Message.MOUSE_DESELECTED,null);
            }
            if(view.currentlyAddingEdge) {
                view.currentlyAddingEdge = false;
            }
            view.currentlySelected = edge;
            edge.inform(Message.MOUSE_CLICKED,null);
        } else {
            if(view.currentlyAddingEdge) {
                view.currentlyAddingEdge = false;
            }
            if ( view.currentlySelected != null ) {
                view.currentlySelected.inform(Message.MOUSE_DESELECTED,new Data(e));
            }
            if (e.isShiftDown() && e.getButton() == MouseEvent.BUTTON1) {
                view.addVertex("#"+IDCounter.next(),e.getX(),e.getY());
            }
        }
        if (e.getButton() == MouseEvent.BUTTON3 ) {
            view.displayPopup(e);
        }
    }
    public void handleMouseDragged(MouseEvent e) {
        view.requestFocus();
        if (e.isShiftDown()) {
            /* TODO : IMPLEMENT MULTIPLE SELECTION */        
        } else {
            VertexPainter node = view.locateVertexPainter(e.getX(),e.getY());
            if (view.currentlyDragged != null) {
              view.currentlyDragged.inform(Message.MOUSE_DRAGGED, new Data(e));
            } else if (node != null) {
                node.inform(Message.MOUSE_DRAGGED, new Data(e));
                view.currentlyDragged = node;
            } else {
                if (view.currentlyDragged == null) {
                    view.dragView(e);
                }
            }
        }
    }
    public void handleMouseMoved(MouseEvent e) {
        VertexPainter node = view.locateVertexPainter(e.getX(),e.getY());
        EdgePainter edge = view.locateEdgePainter(e.getX(),e.getY());
        if (node != null) {
            if (view.currentlyFocused != null) {
                view.currentlyFocused.inform(Message.MOUSE_EXITED,new Data(e));
            }
            view.currentlyFocused = node;
            node.inform(Message.MOUSE_OVER,new Data(e));
        } else if (edge != null) {
            if (view.currentlyFocused != null) {
                view.currentlyFocused.inform(Message.MOUSE_EXITED,new Data(e));
            }
            view.currentlyFocused = edge;
            edge.inform(Message.MOUSE_OVER, null);
        } else {
            if (view.currentlyFocused != null) {
                view.currentlyFocused.inform(Message.MOUSE_EXITED,new Data(e));
                view.currentlyFocused = null;
            }
        }
        view.curX = e.getX();
        view.curY = e.getY();

    }
    
    public boolean newEdgeEnabled() {
    	return true;
    }
    
    static private void setLookAndFeel(String style1, String style2){
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (style1.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }  catch (Exception e) {
                System.out.println("Error: "+style1+" LAF not found.");
        }
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (style2.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error: "+style2+" LAF not found.");
        }
    }
    
    abstract public void launch();
    
}