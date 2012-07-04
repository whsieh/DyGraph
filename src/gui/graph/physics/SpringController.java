package gui.graph.physics;

import util.misc.Vector2D;

public interface SpringController {
   
   final static float DEFAULT_K = 0.05f;
   
   public float k();
   
   public float displacement();
   
   public void update();
   
   public float equilibriumDist();
   
   public boolean inEquilibrium();
   
   public Vector2D force();
   
   public float potentialEnergy();
   
}
