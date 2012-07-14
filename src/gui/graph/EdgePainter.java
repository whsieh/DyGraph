package gui.graph;

import gui.graph.util.Message;
import gui.graph.util.Data;
import gui.graph.physics.ISpringController;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import util.list.InvalidNodeException;
import util.list.ListNode;
import util.misc.LinearEqn2D;
import util.misc.Vector2D;

public class EdgePainter extends AbstractPainter implements ISpringController,IMouseContainer{

    static final int DEFAULT_DISPLACEMENT_ERROR = 4;
    
    static final Color[] EDGE_COLORS = new Color[3];
    static{
        EDGE_COLORS[AbstractPainter.DEFAULT] = Color.GRAY;
        EDGE_COLORS[AbstractPainter.FOCUSED] = Color.DARK_GRAY;
        EDGE_COLORS[AbstractPainter.SELECTED] = Color.BLACK;
    }
    
    /* Graph-related components */
    VertexPainter vp1;
    VertexPainter vp2;
    volatile LinearEqn2D eqn;
    ListNode myListNode;
    
    /* Physics-related components */
    float k;
    float equilibrium;
    volatile float currentLength;

    
    public EdgePainter(GraphViewer graphPane,VertexPainter vp1, VertexPainter vp2,String label){
        
        /* Initializing graph-related components */
        this.myParent = graphPane;
        this.vp1 = vp1;
        this.vp2 = vp2;
        this.eqn = new LinearEqn2D(vp1.x,vp1.y,vp2.x,vp2.y);
        this.updateVertices();
        this.id = label;
        
        /* Initializing physics-related components */
        k = ISpringController.DEFAULT_K; // Change to scale to edge weight.
        equilibrium = myParent.controller.equilibriumLength;
        currentLength = equilibrium;
    }
    
    private void updateVertices() {
        vp1.myEdges.add(this);
        vp2.myEdges.add(this);
    }
    
    @Override
    void inform(Message message, Data e) {
        switch(message){
            
            case MOUSE_OVER:
                if (!(state == AbstractPainter.SELECTED)) {
                    setState(FOCUSED);
                }
                break;
                
            case MOUSE_EXITED:
                if (!(state == AbstractPainter.SELECTED)) {
                    setState(DEFAULT);
                }
                break;
                
            case MOUSE_CLICKED:
                setState(SELECTED);
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
    
    
    
    @Override
    boolean contains(int x, int y) {
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
        g2d.setStroke(new BasicStroke(2));
        
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
        return v.scaleTo(k*displacement());
    }

    @Override
    public float potentialEnergy() {
        return 0.5f * k * (float)Math.pow(displacement(),2);
    }
    
    /*THIS METHOD SUCKS...MAKE IT NOT RETARDED*/
    void remove() {
        try{
            myListNode.remove();
            vp1.myEdges.remove(this);
            vp2.myEdges.remove(this);
        } catch (InvalidNodeException e){
            System.err.println("Error: failed to remove edge due to invalid "
                    + "listnode reference.");
        }
    }

	@Override
	void paintDefault(Graphics g) {
		paintLine(g, EDGE_COLORS[AbstractPainter.DEFAULT]);
	}

	@Override
	void paintFocused(Graphics g) {
		paintLine(g, EDGE_COLORS[AbstractPainter.FOCUSED]);
	}

	@Override
	void paintSelected(Graphics g) {
		paintLine(g, EDGE_COLORS[AbstractPainter.SELECTED]);
	}

    
}
