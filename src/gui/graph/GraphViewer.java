
package gui.graph;

import gui.graph.GraphData.IEdgeData;
import gui.graph.GraphData.IVertexData;
import gui.graph.util.IDCounter;
import gui.graph.physics.IPhysicsController;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.graph.Graph;
import util.list.DLinkedList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import model.graph.Edge;
import model.graph.exception.GraphException;
import util.dict.CoordinateTable2D;
import util.misc.Vector2D;

/* View-model invariant (please note). Each AbstractPainter in this graph should have an id field that
 * matches its corresponding object in the graph model.
 * */

public class GraphViewer extends JPanel {
	
	protected static final boolean MOUSE_EVENT_TESTING = false;
    protected static final boolean PHYSICS_TESTING = false;
    protected static final boolean VERTEXTABLE_TESTING = true;
    
    protected static final boolean RUN_PHYSICS = true;
    
    protected static final int DEFAULT_WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().width * 0.75);
    protected static final int DEFAULT_HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().height * 0.75);
    protected static final int SCREEN_SIZE_MULT = 4;
    
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
    
    protected boolean currentlyAddingEdge;
    
    protected CoordinateTable2D<VertexPainter> vertexTable;
    protected Map<String,VertexPainter> vertexPainterMap;
    protected DLinkedList<VertexPainter> vertexList;
    protected AbstractPainter currentlyFocused,currentlySelected,currentlyDragged;
    protected DLinkedList<EdgePainter> edgeList;
    
    protected int curX;
    protected int curY;
    protected long prevArrowEvent;
    protected int arrowEventCount;
    
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
        vertexTable = new CoordinateTable2D(bounds);
        vertexList = new DLinkedList<VertexPainter>();
        vertexPainterMap = new HashMap<String,VertexPainter>();
        edgeList = new DLinkedList<EdgePainter>();
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)));
        popupX = 0;
        popupY = 0;
        createContextMenu();
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
        
        this.setBackground(Color.WHITE);
        VERTEX_MENUITEMS[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeVertex((VertexPainter)currentlySelected);
            }
        });
        EDGE_MENUITEMS[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeEdge((EdgePainter)currentlySelected);
            }
        });
        VERTEX_MENUITEMS[1].addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               currentlyAddingEdge = true;
           }
        });
    }
    
    public void activate() {
        initiatePhysics();
        initiateAnimation();
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

    
    private void initiateAnimation() {
        Animator.animate(this);
    }
    
    private void initiatePhysics() {
        if (RUN_PHYSICS) {
            GraphPhysicsSimulator.runPhysics(this);
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

    @Override
    public void update(Graphics g) {
        paint(g);
    } 
    
    public void paintFrame(Graphics g) {
    	
        for(EdgePainter ep : edgeList) {
            ep.paint(g);
        }
        if (currentlyAddingEdge) {
            Graphics2D g2d = ((Graphics2D)g);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            VertexPainter vp = ((VertexPainter)currentlySelected);
            Color oldcolor = g.getColor();
            Stroke oldStroke = g2d.getStroke();
            g.setColor(Color.RED);
            if (currentlyFocused != null && currentlyFocused instanceof VertexPainter) {
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
    
    public void refreshVertexTable() {
        vertexTable = new CoordinateTable2D(new Rectangle(
                -DEFAULT_WIDTH*SCREEN_SIZE_MULT,-DEFAULT_HEIGHT*SCREEN_SIZE_MULT,
                2*DEFAULT_WIDTH*SCREEN_SIZE_MULT,2*DEFAULT_HEIGHT*SCREEN_SIZE_MULT));
        for (VertexPainter vp : vertexList) {
            vertexTable.insert(new Point(vp.x,vp.y),vp);
        }
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
    			Edge e = graph.findEdge(eid);
	    		if (e == null) {
	    			addEdge(eid,vp1,vp2,ed.weight());
	    		} else {
	    			e.addWeight(ed.weight());
	    		}
    		}
    	}
    }
    
    public void dragView(MouseEvent e){
        int deltaX = e.getX() - curX;
        int deltaY = e.getY() - curY;
        curX = e.getX();
        curY = e.getY();
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
        if (graph.removeVertex(vp.id) != null) {
            vp.remove();
            vertexPainterMap.remove(vp.id);
        }
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
            return addEdge(eName,vp1,vp2,1.0);
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    
    public EdgePainter addEdge(String id, VertexPainter vp1, VertexPainter vp2, double weight){
        try {
            graph.addEdge(id, vp1.id,vp2.id,Edge.UNDIRECTED, weight);
            EdgePainter e = new EdgePainter(this,vp1,vp2,id);
            edgeList.insertBack(e);
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
            return addEdge(eName,vp1,vp2,1.0);
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
}

class GraphPhysicsSimulator implements IPhysicsController {
    
    static final int CYCLES_PER_SECOND = 60;
    static final int DEFAULT_FRAME_TIME_MS = 1000/CYCLES_PER_SECOND;
    
    GraphViewer g;
    
    private GraphPhysicsSimulator(GraphViewer g){
        this.g = g;   
    }
    
    @Override
    public void run() {
        while(true) {
            int ELAPSED_MS = (int)runPhysicsCycle()/1000000;
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
    
    static void runPhysics(GraphViewer g) {
        new Thread(new GraphPhysicsSimulator(g)).start();
    }
    
    /**
     * Calculates inverse-square repulsive force between two vertices.
     */
    private Vector2D repulsiveForce(VertexPainter vp1, VertexPainter vp2) {
        float squareDist = (float)(Math.pow(vp2.x-vp1.x,2)+Math.pow(vp2.y-vp1.y,2));
        return new Vector2D((vp2.x-vp1.x)/squareDist,(vp2.y-vp1.y)/squareDist,
            Vector2D.CARTESIAN,Vector2D.FORCE).scaleTo(g.controller.repulsiveConstant);
    }
    
    @Override
    public long runPhysicsCycle() {
        long start = System.nanoTime();
        //System.out.println("Running physics cycle:");
        for(EdgePainter spring : g.edgeList) {
            spring.update();
            //System.out.println("  Checking " + spring);
            Vector2D force = spring.force();
            //System.out.println("    Total force: " + force);
            spring.vp1.updateAcceleration(force);
            spring.vp2.updateAcceleration(force.scaleTo(-1));
        }
        for(VertexPainter m1 : g.vertexList) {
            for(VertexPainter m2 : g.vertexList) {
                if (m1 != m2) {
                    m1.acceleration.add(repulsiveForce(m1,m2).
                            scaleTo(-1/m1.mass()));
                }
            }
            m1.calc(DEFAULT_TIMESTEP_MS);
        }
        return System.nanoTime() - start;
    }
    
}

class Animator implements Runnable{

    GraphViewer g;
    
    private Animator(GraphViewer g){
        this.g = g;   
    }
    
    static void animate(GraphViewer g) {
        new Thread(new Animator(g)).start();
    }
    
    @Override
    public void run() {
        Graphics2D g2d = (Graphics2D)g.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        int frames = 0;
        while(true) {
            g.repaint();
            try {
                Thread.sleep(15);
            } catch (InterruptedException e) {
            }
            frames++;
        }
    }
}



