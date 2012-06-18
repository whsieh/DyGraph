
package gui.graph.physics;


public interface PhysicsController extends Runnable{
    
    final static float DEFAULT_TIMESTEP_MS = 12;
    final static float MAX_REPEL_DIST_SQUARED = (float)Math.pow(350,2);
    
    public long runPhysicsCycle();
}
