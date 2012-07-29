package gui.graph;

import java.awt.Point;

public interface IMouseContainer {
    
    public boolean contains(Point p);
    
    public boolean contains(int x, int y);
    
}
