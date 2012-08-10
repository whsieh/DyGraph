package gui.graph;

import gui.graph.physics.ISpringController;
import gui.graph.util.Data;
import gui.graph.util.Message;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;

import model.graph.Edge;
import util.list.InvalidNodeException;
import util.list.ListNode;
import util.misc.LinearEqn2D;
import util.misc.Vector2D;
import dygraph.DygraphConsole;

public class EdgePainter extends AbstractPainter implements ISpringController,IMouseContainer{

    protected static final int DEFAULT_DISPLACEMENT_ERROR = 4;
    protected static final float DEFAULT_DASH_PHASE = 1f;
    protected static final float[] DEFAULT_DASH = new float[] {5f};
    
    protected static final Color[] EDGE_COLORS = new Color[5];
    static{
        EDGE_COLORS[AbstractPainter.DEFAULT] = Color.GRAY;
        EDGE_COLORS[AbstractPainter.HIGHLIGHTED] = Color.BLUE;
        EDGE_COLORS[AbstractPainter.FOCUSED] = Color.DARK_GRAY;
        EDGE_COLORS[AbstractPainter.ACCENTUATED] = Color.RED;
        EDGE_COLORS[AbstractPainter.SELECTED] = Color.BLACK;
    }
    
    /* Graphics-related components */
    protected BasicStroke defaultStroke;
    protected BasicStroke focusedStroke;
    
    /* Graph-related components */
    protected VertexPainter vp1;
    protected VertexPainter vp2;
    protected LinearEqn2D eqn;
    protected ListNode myListNode;
    protected GraphViewer myParent;
    
    /* Physics-related components */
    protected float weight;
    protected float k;
    protected float equilibrium;
    protected volatile float currentLength;

    
    public EdgePainter(GraphViewer graphPane,VertexPainter vp1, VertexPainter vp2,String label){
        
        this(graphPane,vp1, vp2,label,1.0f);
    }
    
    public EdgePainter(GraphViewer graphPane,VertexPainter vp1, VertexPainter vp2,String label, float weight){
        
        /* Initializing graph-related components */
        this.myParent = graphPane;
        this.vp1 = vp1;
        this.vp2 = vp2;
        this.eqn = new LinearEqn2D(vp1.x,vp1.y,vp2.x,vp2.y);
        this.updateVertices();
        this.id = label;
        this.weight = weight;
        this.updateStrokes();
        /* Initializing physics-related components */
        k = ISpringController.DEFAULT_K; // Change to scale to edge weight.
        equilibrium = myParent.controller.equilibriumLength;
        currentLength = equilibrium;
    }
    
    protected void updateStrokes() {
    	float width = weight > 8 ? 8 : weight;
        //this.defaultStroke = new BasicStroke(width,BasicStroke.CAP_BUTT,
        //		BasicStroke.JOIN_BEVEL,10f,DEFAULT_DASH,DEFAULT_DASH_PHASE);
    	this.defaultStroke = new BasicStroke(width);
        this.focusedStroke = new BasicStroke(width);
    }
    
    public void setWeight(float weight) {
    	this.weight = weight;
    	updateStrokes();
    }
    
    public void addWeight(float weight) {
    	this.weight += weight;
    	updateStrokes();
    }
    
    public VertexPainter[] getConnectingVertices() {
    	return new VertexPainter[] {vp1,vp2};
    }
    
    private void updateVertices() {
        vp1.myEdges.add(this);
        vp2.myEdges.add(this);
    }
    
    @Override
	public void inform(Message message, Data e) {
        switch(message){
            
            case MOUSE_OVER:
                if (!(state == AbstractPainter.SELECTED || state == AbstractPainter.ACCENTUATED)) {
                    setState(FOCUSED);
                }
                break;
                
            case MOUSE_EXITED:
                if (!(state == AbstractPainter.SELECTED || state == AbstractPainter.ACCENTUATED)) {
                    setState(DEFAULT);
                }
                break;
                
            case MOUSE_CLICKED:
                setState(SELECTED);
                DygraphConsole.tryLog("You have selected item " + id + "; weight=" + weight);
                break;
                
            case MOUSE_DRAGGED:
                break;
               
            case MOUSE_DESELECTED:
                setState(DEFAULT);
                break;
                
            case REQUEST_UPDATE:
                eqn.updateBounds(vp1.x,vp1.y,vp2.x,vp2.y);
                break;
                
            default:
                System.err.println("Warning: NodePainter received unrecognized"
                        + " user feedback.");
                break;
        }
    }
    
    public Edge getEdge() {
    	return myParent.graph.findEdge(id);
    }
    
    @Override
    public boolean contains(int x, int y) {
        return eqn.contains(x, y);
    }
    @Override
    public boolean contains(Point p) {
        return contains(p.x,p.y);
    }
    
    @Override
    public String toString(){
        String s = vp1 + " -> " + vp2;
        return s;
    }

    private void paintLine(Graphics g, Color c) {
    	
        Graphics2D g2d = (Graphics2D)g;
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
        
        Stroke origStroke = ((Graphics2D)g).getStroke();
        Color origColor = g.getColor();
        
        g.setColor(c);
        
        if (state == DEFAULT) {
        	g2d.setStroke(defaultStroke);
        } else {
        	g2d.setStroke(focusedStroke);
        }
        
        g.drawLine(vp1.x,vp1.y,vp2.x,vp2.y);
        
        g.setColor(origColor);
        g2d.setStroke(origStroke);
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_OFF);
        
    }

    public void updateEquilibrium() {
        equilibrium = 5 * (float)Math.log(vp1.mass() * vp2.mass());
    }
    
    @Override
    public float k() {
        return k;
    }

    @Override
    public float displacement() {
        return currentLength - equilibriumDist();
    }

    @Override
    public void update() {
        currentLength = (float)(Math.pow(Math.pow(vp2.x-vp1.x,2)+
                Math.pow(vp2.y-vp1.y,2),0.5));
    }

    @Override
    public float equilibriumDist() {
        return myParent.controller.equilibriumLength;
    }

    @Override
    public boolean inEquilibrium() {
        float displacement = displacement();
        return -DEFAULT_DISPLACEMENT_ERROR < displacement &&
                displacement < DEFAULT_DISPLACEMENT_ERROR;
    }

    @Override
    public Vector2D force() {
        Vector2D v = LinearEqn2D.toUnitVector(eqn,Vector2D.FORCE);
        // System.out.println("    Unit vector: " + v);
        return weight > 7 ? v.scaleTo(2*k*displacement()) : v.scaleTo(k*displacement());
    }

    @Override
    public float potentialEnergy() {
        return 0.5f * k * (float)Math.pow(displacement(),2);
    }
    
    /* TODO THIS METHOD SUCKS...MAKE IT NOT RETARDED*/
    protected void remove() {
        try{
            myListNode.remove();
            vp1.myEdges.remove(this);
            vp2.myEdges.remove(this);
            myParent.edgePainterMap.remove(id);
            myParent.graph.removeEdge(id);
        } catch (InvalidNodeException e){
            System.err.println("Error: failed to remove edge due to invalid "
                    + "listnode reference.");
        }
    }

	@Override
	protected void paintDefault(Graphics g) {
		paintLine(g, EDGE_COLORS[AbstractPainter.DEFAULT]);
	}
	
	@Override
	protected void paintHighlighted(Graphics g) {
		paintLine(g, EDGE_COLORS[AbstractPainter.HIGHLIGHTED]);
	}

	@Override
	protected void paintFocused(Graphics g) {
		paintLine(g, EDGE_COLORS[AbstractPainter.FOCUSED]);
	}
	
	@Override
	protected void paintAccentuated(Graphics g) {
		paintLine(g, EDGE_COLORS[AbstractPainter.ACCENTUATED]);
	}

	@Override
	protected void paintSelected(Graphics g) {
		paintLine(g, EDGE_COLORS[AbstractPainter.SELECTED]);
	}

    
}
