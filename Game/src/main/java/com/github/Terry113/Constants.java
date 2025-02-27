package com.github.Terry113;

/**
 * Global constants used throughout the simulation.
 */
public class Constants {
    
    // Unit constants
    public static final int DETECTION_RADIUS = 50;
    public static final float STEERING_DAMPING = 0.1f; // Damping factor for steering
    
    // Field constants
    public static final int FIELD_WIDTH = 800;
    public static final int FIELD_HEIGHT = 600;
    
    // Quadtree constants
    public static final int QUADTREE_MAX_DEPTH = 4; // Maximum depth of quadtree
    public static final int QUADTREE_MAX_OBJECTS = 4; // Maximum objects per node before splitting
}