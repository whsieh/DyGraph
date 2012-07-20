package gui.graph.physics;

import util.misc.Vector2D;

public interface IMassController {
    
    final static float ENERGY_LOSS_COEFFICIENT = -0.0025f;
    
    public float mass();
    
    public Vector2D position();
    
    public Vector2D velocity();
    
    public Vector2D acceleration();
    
    public float kineticEnergy();
    
    public boolean inEquilibrium();
    
    public void updateAcceleration(Vector2D force);
    
    public void calc(float dt);
    
    
}