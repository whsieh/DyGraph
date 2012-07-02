
package gui.graph;

import gui.graph.util.IDCounter;
import gui.graph.physics.PhysicsController;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.graph.Graph;
import util.list.DLinkedList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import model.graph.Edge;
import util.dict.CoordinateTable2D;
import util.misc.Vector2D;

// TODO Write methods that add data into the graph model and can request data
// from the graph model. This kind of ganks standard MVC framework by making the
// controller talk to the view and the view talk to the model, but it should
// be enough to keep our project clean and organized.

public class GViewer extends JPanel {
	
    static final boolean MOUSE_EVENT_TESTING = false;
    static final boolean PHYSICS_TESTING = false;
    static final boolean VERTEXTABLE_TESTING = true;
    
    static final boolean RUN_PHYSICS = true;
    
    static final int DEFAULT_WIDTH = (int)(Toolkit.getDefaultToolkit().getScreenSize().width * 0.75);
    static final int DEFAULT_HEIGHT = (int)(Toolkit.getDefaultToolkit().getScreenSize().height * 0.75);
    static final int SCREEN_SIZE_MULT = 4;
    
    int popupX,popupY;
    final JPopupMenu WHITESPACE_POPUPMENU;
    final JPopupMenu VERTEX_POPUPMENU;
    final JPopupMenu EDGE_POPUPMENU;
    final JMenuItem[] WHITESPACE_MENUITEMS;
    final JMenuItem[] VERTEX_MENUITEMS;
    final JMenuItem[] EDGE_MENUITEMS;

    static float repulsive_constant = (float)Math.pow(2,3);
    static float equilibrium_length = 150;
    static int unit_mass = 400;
    
    boolean currentlyAddingEdge;
    InfoDisplay infoDisplay;
    
    CoordinateTable2D<VertexPainter> vertexTable;
    DLinkedList<VertexPainter> vertexList;
    AbstractPainter currentlyFocused,currentlySelected,currentlyDragged;
    DLinkedList<EdgePainter> edgeList;
    
    int curX;
    int curY;
    long prevArrowEvent;
    int arrowEventCount;
    
    Rectangle bounds;
    Graph graph;
    GController controller;
    
    public GViewer(GController controller){
        
        super();
        this.setFocusable(true);
        this.infoDisplay = new InfoDisplay(this);
        this.graph = new Graph("New Graph");
        this.controller = controller;
        this.currentlyAddingEdge = false;
        this.bounds = new Rectangle(-DEFAULT_WIDTH*SCREEN_SIZE_MULT,
                -DEFAULT_HEIGHT*SCREEN_SIZE_MULT,2*DEFAULT_WIDTH*SCREEN_SIZE_MULT,
                2*DEFAULT_HEIGHT*SCREEN_SIZE_MULT);
        vertexTable = new CoordinateTable2D(bounds);
        vertexList = new DLinkedList<VertexPainter>();
        edgeList = new DLinkedList<EdgePainter>();
        this.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createBevelBorder(BevelBorder.RAISED),
                BorderFactory.createBevelBorder(BevelBorder.LOWERED)));
        popupX = 0;
        popupY = 0;

        add(infoDisplay);
        infoDisplay.setVisible(true);
        
        
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
        this.infoDisplay.setLocation(10,10);
    }
    
    void displayPopup(MouseEvent me) {
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
    
    void resetVertexTable() {
        vertexTable = new CoordinateTable2D(new Rectangle(
                -DEFAULT_WIDTH*SCREEN_SIZE_MULT,-DEFAULT_HEIGHT*SCREEN_SIZE_MULT,
                2*DEFAULT_WIDTH*SCREEN_SIZE_MULT,2*DEFAULT_HEIGHT*SCREEN_SIZE_MULT));
        for (VertexPainter vp : vertexList) {
            vertexTable.insert(new Point(vp.x,vp.y),vp);
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
        infoDisplay.setLocation(infoDisplay.getX()+deltaX,infoDisplay.getY()+deltaY);
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
                infoDisplay.setLocation(infoDisplay.getX()-amount,infoDisplay.getY());
                break;
            case KeyEvent.VK_LEFT:
                for(VertexPainter vp : vertexList){
                    vp.moveTo(vp.x+amount,vp.y);
                }
                infoDisplay.setLocation(infoDisplay.getX()+amount,infoDisplay.getY());
                break;
            case KeyEvent.VK_DOWN:
                for(VertexPainter vp : vertexList){
                    vp.moveTo(vp.x,vp.y-amount);
                }
                infoDisplay.setLocation(infoDisplay.getX(),infoDisplay.getY()-amount);
                break;
            case KeyEvent.VK_UP:
                for(VertexPainter vp : vertexList){
                    vp.moveTo(vp.x,vp.y+amount);
                }
                infoDisplay.setLocation(infoDisplay.getX(),infoDisplay.getY()+amount);
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
    
    void removeVertex(VertexPainter vp) {
        if (graph.removeVertex(vp.id) != null) {
            vp.remove();
        }
    }
    
    void removeEdge(EdgePainter ep) {
        if (graph.removeEdge(ep.id) != null) {
            ep.remove();
        }
    }
    
    public VertexPainter addVertex(String label,int x, int y){
        try {
            graph.addVertex(label);
            VertexPainter vp = new VertexPainter(this,x,y,label);
            vertexTable.insert(new Point(x,y),vp);
            vertexList.insertBack(vp);
            vp.myListNode = vertexList.back();
            return vp;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    
    public EdgePainter addEdge(VertexPainter vp1, VertexPainter vp2){
        try {
            String eName = "<" + vp1.id + "," + vp2.id + ">";
            graph.addEdge(eName, vp1.id,vp2.id,Edge.UNDIRECTED);
            EdgePainter e = new EdgePainter(this,vp1,vp2,eName);
            edgeList.insertBack(e);
            e.myListNode = edgeList.back();
            return e;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }
    
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
                    Logger.getLogger(GViewer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (anch != 0) {
                addEdge(anchors[anch],anchors[anch-1]);
            }
        }
    }
    static VertexPainter testSmallWorld(GViewer panel,int numVertices,int localDist) {
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
    
    static void testRandom(GViewer panel,int test_vert_count,float test_e_r_prob) {
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
    static void testClusters(GViewer panel) {
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
class GraphPhysicsSimulator implements PhysicsController {
    
    static final int CYCLES_PER_SECOND = 60;
    static final int DEFAULT_FRAME_TIME_MS = 1000/CYCLES_PER_SECOND;
    
    GViewer g;
    
    private GraphPhysicsSimulator(GViewer g){
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
    
    static void runPhysics(GViewer g) {
        new Thread(new GraphPhysicsSimulator(g)).start();
    }
    
    /**
     * Calculates inverse-square repulsive force between two vertices.
     */
    private Vector2D repulsiveForce(VertexPainter vp1, VertexPainter vp2) {
        float squareDist = (float)(Math.pow(vp2.x-vp1.x,2)+Math.pow(vp2.y-vp1.y,2));
        if (squareDist < MAX_REPEL_DIST_SQUARED) {
            return new Vector2D((vp2.x-vp1.x)/squareDist,(vp2.y-vp1.y)/squareDist,
                Vector2D.CARTESIAN,Vector2D.FORCE).scaleTo(g.controller.repulsiveConstant);
        } else {
            return new Vector2D(0,0,Vector2D.CARTESIAN,Vector2D.FORCE);
        }
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

    GViewer g;
    
    private Animator(GViewer g){
        this.g = g;   
    }
    
    static void animate(GViewer g) {
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



