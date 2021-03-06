
package gui.graph;

import gui.graph.GraphData.IEdgeData;
import gui.graph.GraphData.IVertexData;
import gui.graph.physics.IPhysicsController;
import gui.graph.util.IDCounter;
import gui.graph.util.Message;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import model.graph.Edge;
import model.graph.Graph;
import util.dict.CoordinateTable2D;
import util.list.DLinkedList;
import util.misc.Vector2D;

/* View-model invariant (please note). Each AbstractPainter in this graph should have an id field that
 * matches its corresponding object in the graph model.
 * */

public class GraphViewer extends JPanel {
	
	protected static final boolean MOUSE_EVENT_TESTING = false;
    protected static final boolean PHYSICS_TESTING = false;
    protected static final boolean VERTEXTABLE_TESTING = true;
    
    protected static final boolean RUN_PHYSICS = true;
    
    public static final int DEFAULT_WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().width);
    public static final int DEFAULT_HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().height);
    public static final int SCREEN_SIZE_MULT = 4;
    
    protected int popupX,popupY;
    
    protected JPopupMenu WHITESPACE_POPUPMENU;
    protected JPopupMenu VERTEX_POPUPMENU;
    protected JPopupMenu EDGE_POPUPMENU;
    protected JMenuItem[] WHITESPACE_MENUITEMS;
    protected JMenuItem[] VERTEX_MENUITEMS;
    protected JMenuItem[] EDGE_MENUITEMS;

    protected static float repulsive_constant = (float)Math.pow(2,12);
    protected static float equilibrium_length = 0;
    protected static int unit_mass = 512;
    protected static int heavy_mass = 3000;
    
    protected boolean currentlyAddingEdge;
    protected boolean draggingView;
    
    protected CoordinateTable2D<VertexPainter> vertexTable;
    protected Map<String,VertexPainter> vertexPainterMap;
    protected Map<String,EdgePainter> edgePainterMap;
    protected DLinkedList<VertexPainter> vertexList;
    protected AbstractPainter currentlyFocused;
	protected AbstractPainter currentlySelected;
	protected AbstractPainter currentlyDragged;
    protected DLinkedList<EdgePainter> edgeList;
    
    protected int curX;
    protected int curY;
    protected long prevArrowEvent;
    protected int arrowEventCount;
    
    protected int halfWidth;
    protected int halfHeight;
    
    protected Rectangle bounds;
    protected Graph graph;
    protected GraphController controller;
    
    static public int[] getRandomPoints() {
    	return new int[] {(int)(DEFAULT_WIDTH*Math.random()),
    					(int)(DEFAULT_HEIGHT*Math.random())
			};
	}

    public GraphViewer(GraphController controller){
        
        super();
        this.setFocusable(true);
        this.graph = new Graph("New Graph");
        this.controller = controller;
        this.currentlyAddingEdge = false;
        this.bounds = new Rectangle(-DEFAULT_WIDTH*SCREEN_SIZE_MULT,
                -DEFAULT_HEIGHT*SCREEN_SIZE_MULT,2*DEFAULT_WIDTH*SCREEN_SIZE_MULT,
                2*DEFAULT_HEIGHT*SCREEN_SIZE_MULT);
        vertexTable = new CoordinateTable2D<VertexPainter>(bounds);
        vertexList = new DLinkedList<VertexPainter>();
        vertexPainterMap = new HashMap<String,VertexPainter>();
        edgePainterMap = new HashMap<String,EdgePainter>();
        edgeList = new DLinkedList<EdgePainter>();
        popupX = 0;
        popupY = 0;
        halfWidth = controller.root.getX() + controller.root.getWidth()/2;
        halfHeight = controller.root.getY() + controller.root.getHeight()/2;
        
        curX = halfWidth;
        curY = halfHeight;
        createContextMenu();
        setBackground(Color.WHITE);
    }
    
    protected void createContextMenu() {
    	
        WHITESPACE_POPUPMENU = new JPopupMenu();
        VERTEX_POPUPMENU = new JPopupMenu();
        EDGE_POPUPMENU = new JPopupMenu();
        WHITESPACE_MENUITEMS = new JMenuItem[] {new JMenuItem("New vertex")};
        VERTEX_MENUITEMS = new JMenuItem[] {new JMenuItem("Delete vertex"),
            new JMenuItem("Add edge")};
        EDGE_MENUITEMS = new JMenuItem[] {new JMenuItem("Delete edge")};
        
        for(int i = 0; i < WHITESPACE_MENUITEMS.length; i++) {
            WHITESPACE_POPUPMENU.add(WHITESPACE_MENUITEMS[i]);
        }
        for(int i = 0; i < VERTEX_MENUITEMS.length; i++) {
            VERTEX_POPUPMENU.add(VERTEX_MENUITEMS[i]);
        }
        for(int i = 0; i < EDGE_MENUITEMS.length; i++) {
            EDGE_POPUPMENU.add(EDGE_MENUITEMS[i]);
        }
        WHITESPACE_MENUITEMS[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addVertex("#" + IDCounter.next(),popupX,popupY);
            }
        });
        VERTEX_MENUITEMS[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeVertex((VertexPainter)getCurrentlySelected());
            }
        });
        EDGE_MENUITEMS[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeEdge((EdgePainter)getCurrentlySelected());
            }
        });
        VERTEX_MENUITEMS[1].addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               currentlyAddingEdge = true;
           }
        });
    }
    
    @Override
    public Rectangle bounds() {
		return bounds;
    }
    
    public void updateMidpoint() {
        halfWidth = controller.root.getX() + controller.root.getWidth()/2;
        halfHeight = controller.root.getY() + controller.root.getHeight()/2;
    }
    
    public void activate() {
        initiatePhysics();
        initiateAnimation();
    }
    
    public boolean isDraggingView() {
    	return draggingView;
    }
    
    public void setDraggingView(boolean b) {
    	this.draggingView = b;
    }
    
    public void setCurPosition(int x, int y) {
    	curX = x;
    	curY = y;
    }
    
    protected void displayPopup(MouseEvent me) {
        popupX = me.getX();
        popupY = me.getY();
        AbstractPainter v = locateVertexPainter(popupX,popupY);
        AbstractPainter e = locateEdgePainter(popupX,popupY);
        if (v == null && e == null) {
            WHITESPACE_POPUPMENU.show(me.getComponent(),
                       popupX, popupY);
        } else if (v != null) {            
            VERTEX_POPUPMENU.show(me.getComponent(),
                       popupX, popupY);
        } else if (e != null) {
            EDGE_POPUPMENU.show(me.getComponent(),
                       popupX, popupY);
        }
    }

    
    protected void initiateAnimation() {
    	new Thread(new Animator()).start();
    }
    
    protected void initiatePhysics() {
        if (RUN_PHYSICS) {
        	new Thread(new GraphPhysicsSimulator()).start();
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DEFAULT_WIDTH,DEFAULT_HEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintFrame(g);
    }
    
    public void paintFrame(Graphics g) {
    	
        for(EdgePainter ep : edgeList) {
            ep.paint(g);
        }
        if (currentlyAddingEdge) {
            Graphics2D g2d = ((Graphics2D)g);
            VertexPainter vp = ((VertexPainter)getCurrentlySelected());
            Color oldcolor = g.getColor();
            Stroke oldStroke = g2d.getStroke();
            g.setColor(Color.RED);
            if (getCurrentlyFocused() != null && getCurrentlyFocused() instanceof VertexPainter) {
                g2d.setStroke(new BasicStroke(3));
            }
            g.drawLine(vp.x,vp.y,curX,curY);
            g.setColor(oldcolor);
            g2d.setStroke(oldStroke);
        }
        for(VertexPainter np : vertexList){
            np.paint(g);
        }
    }
    
    public Graph getGraph() {
    	return graph;
    }
    
    public void addGraphData(GraphData<? extends IVertexData,? extends IEdgeData> data) {
    	for (IVertexData vd : data.getVertexInfo()) {
    		String vid = vd.getID();
    		if (graph.findVertex(vid) == null) {
    			int[] randomPoints = GraphViewer.getRandomPoints();
    			this.addVertex(vid, randomPoints[0], randomPoints[1]);
    		}
    	}
    	for (IEdgeData ed : data.getEdgeInfo()) {
    		String eid = ed.getID();
    		String[] vid = ed.getVertexID();
    		if (graph.findVertex(vid[0]) != null && graph.findVertex(vid[1]) != null) {
    			VertexPainter vp1 = vertexPainterMap.get(vid[0]);
    			VertexPainter vp2 = vertexPainterMap.get(vid[1]);
    			EdgePainter ep = edgePainterMap.get(eid);
    			Edge e = graph.findEdge(eid);
	    		if (e == null && ep == null) {
	    			addEdge(eid,vp1,vp2,ed.weight());
	    		} else if (e != null && ep != null){
	    			e.addWeight(ed.weight());
	    			ep.addWeight(ed.weight());
	    		}
    		}
    	}
    }
    
    public void dragView(MouseEvent e){
        int deltaX = e.getX() - curX;
        int deltaY = e.getY() - curY;
        for(VertexPainter vp : vertexList){
        	vp.moveTo(vp.x+deltaX,vp.y+deltaY);
        }
    }
    
    public void dragView(int deltaX, int deltaY){
        for(VertexPainter vp : vertexList){
            vp.moveTo(vp.x+deltaX,vp.y+deltaY);
        }
    }
    
    public void dragView(KeyEvent e){
        
        long delay = e.getWhen() - prevArrowEvent;        
        prevArrowEvent = e.getWhen();
        if (delay < 100) {
            arrowEventCount += 1;
        } else {
            arrowEventCount = 0;
        }
        int amount = arrowEventCount + 2;
        switch(e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                for(VertexPainter vp : vertexList){
                    vp.moveTo(vp.x-amount,vp.y);
                }
                break;
            case KeyEvent.VK_LEFT:
                for(VertexPainter vp : vertexList){
                    vp.moveTo(vp.x+amount,vp.y);
                }
                break;
            case KeyEvent.VK_DOWN:
                for(VertexPainter vp : vertexList){
                    vp.moveTo(vp.x,vp.y-amount);
                }
                break;
            case KeyEvent.VK_UP:
                for(VertexPainter vp : vertexList){
                    vp.moveTo(vp.x,vp.y+amount);
                }
                break;
            default:
                break;            
        }
    }
    
    public VertexPainter locateVertexPainter(int x, int y) {
        return vertexTable.find(new Point(x,y));
    }
    
    public EdgePainter locateEdgePainter(int x, int y) {
        for (EdgePainter ep : edgeList) {
            if (ep.contains(x, y)) {
                return ep;
            }
        }
        return null;
    }
    
    public void removeVertex(VertexPainter vp) {
        removeVertex(vp.id);
    }
    
    public void removeVertex(String id) {
        if (graph.removeVertex(id) != null) {
            VertexPainter vp = vertexPainterMap.remove(id);
            vp.remove();
        }
    }
    
    public void removeEdge(EdgePainter ep) {
        if (graph.removeEdge(ep.id) != null) {
            ep.remove();
        }
    }
    
    public VertexPainter addVertex(String id,int x, int y){
        return addVertex(id,x,y,id);
    }
    
    public VertexPainter addVertex(String id,int x, int y, String displayName){
        try {
            graph.addVertex(id);
            VertexPainter vp = createVertexPainter(x,y,id,displayName);
            vertexTable.insert(new Point(x,y),vp);
            vertexList.insertBack(vp);
            vp.myListNode = vertexList.back();
            vertexPainterMap.put(id,vp);
            return vp;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    
    public EdgePainter addEdge(String id, VertexPainter vp1, VertexPainter vp2){
        try {
            String eName = "<" + vp1.id + "," + vp2.id + ">";
            return addEdge(eName,vp1,vp2,1.0f);
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    
    public EdgePainter addEdge(String id, VertexPainter vp1, VertexPainter vp2, float weight){
        try {
            graph.addEdge(id, vp1.id,vp2.id,Edge.UNDIRECTED, weight);
            EdgePainter e = new EdgePainter(this,vp1,vp2,id, weight);
            edgeList.insertBack(e);
            edgePainterMap.put(id,e);
            e.myListNode = edgeList.back();
            return e;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    
    public EdgePainter addEdge(VertexPainter vp1, VertexPainter vp2){
        try {
            String eName = "<" + vp1.id + "," + vp2.id + ">";
            return addEdge(eName,vp1,vp2,1.0f);
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    
    protected VertexPainter createVertexPainter(int x, int y, String id, String displayName) {
    	return new VertexPainter(this,x,y,id,displayName);
    }
    
    @Deprecated
    public void testTree(int cluster_vert_count,int anchor_vert_count) {
        VertexPainter[][] clusters = new VertexPainter[anchor_vert_count][cluster_vert_count];
        VertexPainter[] anchors = new VertexPainter[anchor_vert_count];
        for(int anch = 0; anch < anchor_vert_count; anch++) {
            anchors[anch] = addVertex("#"+IDCounter.next(),
                    (int)(Math.random()*DEFAULT_WIDTH),
                (int)(Math.random()*DEFAULT_HEIGHT));
            for(int clust = 0; clust < cluster_vert_count; clust++) {
                clusters[anch][clust] = addVertex(
                        "#"+IDCounter.next(),(int)(Math.random()*DEFAULT_WIDTH),
                (int)(Math.random()*DEFAULT_HEIGHT));
                addEdge(anchors[anch],clusters[anch][clust]);
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GraphViewer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (anch != 0) {
                addEdge(anchors[anch],anchors[anch-1]);
            }
        }
    }
    
    @Deprecated
    static VertexPainter testSmallWorld(GraphViewer panel,int numVertices,int localDist) {
        VertexPainter[] vertices = new VertexPainter[numVertices];
        for(int i = 0; i < numVertices; i++) {
            vertices[i] = panel.addVertex("#"+IDCounter.next(),
                    (int)(Math.random()*DEFAULT_WIDTH),
                (int)(Math.random()*DEFAULT_HEIGHT));
        }
        for(int i = 0; i < numVertices; i++) {
            for(int k = 1; k <= localDist; k++) {
                if(i+k < 0) {
                    panel.addEdge(vertices[i],vertices[i+k+numVertices]);
                } else if (i+k >= numVertices) {
                    panel.addEdge(vertices[i],vertices[i+k-numVertices]);
                } else {
                    panel.addEdge(vertices[i],vertices[i+k]);
                }
            }
        }
        return vertices[0];
    }
    
    @Deprecated
    static void testRandom(GraphViewer panel,int test_vert_count,float test_e_r_prob) {
        VertexPainter[] vertices = new VertexPainter[test_vert_count];
        for(int i = 0; i < test_vert_count; i++) {            
            vertices[i] = panel.addVertex("#"+IDCounter.next(),
                    (int)(Math.random()*DEFAULT_WIDTH),
                (int)(Math.random()*DEFAULT_HEIGHT));
        }
        for(int i = 0; i < 50; i+=1) {  
            for (int c = i + 1; c < test_vert_count; c++) {
                if (Math.random() < test_e_r_prob )
                    panel.addEdge(vertices[i],vertices[c]);
            }
        }
    }
    
    @Deprecated
    static void testClusters(GraphViewer panel) {
        VertexPainter center = panel.addVertex("#"+IDCounter.next(),
                (int)(Math.random()*DEFAULT_WIDTH),
                (int)(Math.random()*DEFAULT_HEIGHT));
        VertexPainter vp1 = testSmallWorld(panel,6,3);
        VertexPainter vp2 = testSmallWorld(panel,6,3);
        VertexPainter vp3 = testSmallWorld(panel,6,3);
        VertexPainter vp4 = testSmallWorld(panel,6,3);
        VertexPainter vp5 = testSmallWorld(panel,6,3);
        VertexPainter vp6 = testSmallWorld(panel,6,3);
        panel.addEdge(center, vp1);
        panel.addEdge(center, vp2);
        panel.addEdge(center, vp3);
        panel.addEdge(center, vp4);
        panel.addEdge(center, vp5);
        panel.addEdge(center, vp6);
    }

	public AbstractPainter getCurrentlySelected() {
		return currentlySelected;
	}

	public void setCurrentlySelected(AbstractPainter currentlySelected) {
		this.currentlySelected = currentlySelected;
	}

	public AbstractPainter getCurrentlyFocused() {
		return currentlyFocused;
	}

	public void setCurrentlyFocused(AbstractPainter currentlyFocused) {
		this.currentlyFocused = currentlyFocused;
	}
	
	public class GraphPhysicsSimulator implements IPhysicsController {
	    
	    protected static final int CYCLES_PER_SECOND = 100;
	    protected static final int DEFAULT_FRAME_TIME_MS = 1000/CYCLES_PER_SECOND;
	    
	    @Override
	    public void run() {
	        while(true) {
	            int ELAPSED_MS = (int)(runPhysicsCycle()/1000000.0);
	            // System.out.println("Physics: " + ELAPSED_MS + " ms");
	            if (ELAPSED_MS < DEFAULT_FRAME_TIME_MS) {
	                try{
	                    Thread.sleep(DEFAULT_FRAME_TIME_MS - ELAPSED_MS);
	                }catch(Exception e){
	                    e.printStackTrace();
	                    System.exit(1);
	                }
	            }
	        }
	    }
	    
	    /**
	     * Calculates inverse-square repulsive force between two vertices.
	     */
	    protected Vector2D repulsiveForce(VertexPainter vp1, VertexPainter vp2) {
	        float squareDist = (float)(Math.pow(vp2.x-vp1.x,2)+Math.pow(vp2.y-vp1.y,2));
	        return new Vector2D((vp2.x-vp1.x)/squareDist,(vp2.y-vp1.y)/squareDist,
	            Vector2D.CARTESIAN,Vector2D.FORCE).scaleTo(controller.repulsiveConstant);
	    }
	    
	    protected Vector2D autoDragForce() {
	    	int xDiff = curX - halfWidth;
			int yDiff = curY - halfHeight;
			if (xDiff*xDiff + yDiff*yDiff > 122500 ) {
				return new Vector2D(xDiff>>7,yDiff>>7,Vector2D.CARTESIAN,Vector2D.FORCE);
			} else {
				return null;
			}
	    }
	    
	    protected void updateEdges() {
			for (EdgePainter ep : edgePainterMap.values()) {
				ep.inform(Message.REQUEST_UPDATE, null);
			}
	    }
	    
	    protected void calcAllSpringForces() {
	        for(EdgePainter spring : edgeList) {
	            spring.update();
	            //System.out.println("  Checking " + spring);
	            Vector2D force = spring.force();
	            //System.out.println("    Total force: " + force);
	            spring.vp1.updateAcceleration(force);
	            spring.vp2.updateAcceleration(force.scaleTo(-1));
	        }
	    }
	    
	    protected void calcAllRepulsiveForces() {
	        for(VertexPainter m1 : vertexList) {
	            for(VertexPainter m2 : vertexList) {
	                if (m1 != m2) {
	                    m1.acceleration.add(repulsiveForce(m1,m2).
	                            scaleTo(-1/m1.mass()));
	                    if (hasFocus() && !draggingView && currentlyDragged == null) {
		                    Vector2D dragForce = autoDragForce();
		                    if (dragForce != null) {
		                    	m1.acceleration.add(dragForce.scaleTo(-1/m1.mass()));
		                    }
	                    }
	                    
	                }
	            }
	            m1.calc(DEFAULT_TIMESTEP_MS);
	        }
	    }
	    
	    @Override
	    public long runPhysicsCycle() {
	        long start = System.nanoTime();
	        //System.out.println("Running physics cycle:");
	        calcAllSpringForces();
	        calcAllRepulsiveForces();
	        // updateEdges();
	        return System.nanoTime() - start;
	    }
	}
	

	public class Animator implements Runnable{
	    
	    @Override
	    public void run() {
	        Graphics2D g2d = (Graphics2D)getGraphics();
	        while(true) {
	            repaint();
	            try {
	                Thread.sleep(5);
	            } catch (InterruptedException e) {
	            }
	            // System.out.println("Animation: " + (System.nanoTime() - start)/1000000.0);
	        }
	    }
	}
}




