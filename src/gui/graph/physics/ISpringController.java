package gui.graph.physics;

import util.misc.Vector2D;

public interface ISpringController {
   
   final static float DEFAULT_K = 1.2f;
   
   public float k();
   
   public float displacement();
   
   public void update();
   
   public float equilibriumDist();
   
   public boolean inEquilibrium();
   
   public Vector2D force();
   
   public float potentialEnergy();
   
}
