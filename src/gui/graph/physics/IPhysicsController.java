
package gui.graph.physics;


public interface IPhysicsController extends Runnable{
    
    final static float DEFAULT_TIMESTEP_MS = 6;
    @Deprecated
    final static float MAX_REPEL_DIST_SQUARED = (float)Math.pow(750,2);
    
    public long runPhysicsCycle();
}
