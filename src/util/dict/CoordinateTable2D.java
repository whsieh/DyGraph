package util.dict;

import gui.graph.AbstractPainter;
import gui.graph.IMouseContainer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;

public class CoordinateTable2D<V> {
    
    static final public int DIM = 32;
    
    int size;
    int dim;
    List <V>[][] buckets;
    int minX,minY,maxX,maxY;
    int gapX,gapY;
    int xShift,yShift;
    
    public CoordinateTable2D(int minX, int minY, int maxX, int maxY) {
        this.size = 0;
        this.dim = DIM;
        this.buckets = (List<V>[][])
                new List [DIM][DIM];
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.gapX = Math.round(((float)maxX - (float)minX)/dim);
        this.gapY = Math.round(((float)maxY - (float)minY)/dim);
        this.xShift = minX/gapX;
        this.yShift = minY/gapY;
    }
    
    public CoordinateTable2D(Rectangle rect) {
        this(rect.x,rect.y,rect.x+rect.width,rect.y+rect.height);
    }
    
    public Point findRegion(Point p) {
        return new Point(p.x/gapX - xShift,p.y/gapY - yShift);
    }
    
    public Point findRegion(int x, int y) {
        return new Point(x/gapX - xShift,y/gapY - yShift);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Queue<V> findNeighbors(Point key,V v) {
        
        Queue<V> neighbors = new ConcurrentLinkedQueue<V>();
        Point region = findRegion(key);
        int x = region.x, y = region.y;
        if(buckets[x][y] != null) {
            for (V val : buckets[x][y]) {
                    if (val != v) {
                        neighbors.add(val);
                    }
            }
            for (int i = -1; i <= 1; i++) {
                for (int c = -1; c <= 1; c++) {
                    if (isValid(x+c,y+c)) {
                        if (buckets[x+c][y+c] != null){
                            neighbors.addAll(buckets[x+c][y+c]);
                        }
                    }
                }
            }
        }
        return neighbors;
    }

    public V insert(Point key, V value) {
        return insert(key.x, key.y, value);
    }
    
    public V insert(int x, int y, V value) {
        Point region = findRegion(x,y);
        int xReg = region.x, yReg = region.y;
        if (0 <= xReg && xReg < DIM && 0 <= yReg && yReg < DIM) {
	        if (buckets[xReg][yReg] == null) {
	            buckets[xReg][yReg] = 
	                    new CopyOnWriteArrayList <V>();
	        }
	        buckets[xReg][yReg].add(value);
	        size++;
	        return value;
        }
        return null;
    }

    public V find(Point key) {
        Point region = findRegion(key);
        int x = region.x, y = region.y;
        if (0 <= x && x < DIM && 0 <= y && y < DIM) {
	        if (buckets[x][y] == null) {
	            return null;
	        }
	        for (V e : buckets[x][y]) {
	            if (((IMouseContainer)(e)).contains(key)) {
	                return e;
	            }
	        }
        }
        return null;
    }
    
    public V remove(Point key, V v) {
        return remove(key.x, key.y, v);
    }
    
    public V remove(int x, int y, V v) {
        Point region = findRegion(x,y);
        int xReg = region.x, yReg = region.y;
        if (0 <= xReg && xReg < DIM && 0 <= yReg && yReg < DIM) {
	        if (buckets[xReg][yReg] == null) {
	            return null;
	        }
	        for (V e : buckets[xReg][yReg]) {
	            if (((IMouseContainer)(e)).contains(x,y) && v == e) {
	                buckets[xReg][yReg].remove(e);
	                size--;
	                return e;
	            }
	        }
        } else {
        	System.err.println("Vertex out of bounds: " + v);
        }
        return null;
    }
    
    public void paint(Graphics g) {
        g.setColor(Color.GRAY);
        int x = minX + gapX;
        for (int line = 1; line < DIM; line++) {
            g.drawLine(x,minY,x,maxY);
            x += gapX;
        }
        int y = minY + gapY;
        for (int line = 1; line < DIM; line++) {
            g.drawLine(minX,y,maxX,y);
            y += gapY;
        }
        g.setColor(Color.BLACK);
    }

    private boolean isValid(int x, int y) {
        return (x >= 0) && (y >= 0) && (x < DIM) && (y < DIM); 
    }
    
}
