
package gui.graph.physics;


public interface PhysicsController extends Runnable{
    
    final static float DEFAULT_TIMESTEP_MS = 15;
    final static float MAX_REPEL_DIST_SQUARED = (float)Math.pow(750,2);
    
    public long runPhysicsCycle();
}
