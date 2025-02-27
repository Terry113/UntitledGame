package com.github.Terry113;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Base class for all units in the simulation.
 */
public abstract class Unit {
    private float x, y; // Position
    private float vx, vy; // Velocity
    private float angle; // Orientation angle in radians
    private Color color;

    /**
     * Creates a new unit.
     * 
     * @param x Initial X position
     * @param y Initial Y position
     * @param vx Initial X velocity
     * @param vy Initial Y velocity
     * @param angle Initial angle in radians
     * @param color Color of the unit
     */
    public Unit(float x, float y, float vx, float vy, float angle, Color color) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.color = color;
    }

    /**
     * Updates the unit's state for the current frame.
     */
    public abstract void update();

    /**
     * Renders the unit to the screen.
     * 
     * @param g The graphics context to render to
     */
    public abstract void render(Graphics g);

    /**
     * Applies a steering force toward a target position.
     * 
     * @param xTarget X coordinate of the target
     * @param yTarget Y coordinate of the target
     */
    public void steer(float xTarget, float yTarget) {
        double distanceSquared = Util.getDistanceSquared(xTarget, yTarget, x, y);
        
        // Only steer if not too close to target
        if (distanceSquared > 25) {
            // Calculate desired velocity (normalized direction to target)
            float dx = xTarget - x;
            float dy = yTarget - y;
            float distance = (float) Math.sqrt(distanceSquared);
            
            // Normalize
            dx /= distance;
            dy /= distance;
            
            // Calculate steering force (desired velocity - current velocity)
            float steerX = dx - vx;
            float steerY = dy - vy;
            
            // Apply the steering force with damping
            vx += steerX * Constants.STEERING_DAMPING;
            vy += steerY * Constants.STEERING_DAMPING;
            
            // Update the angle
            angle = (float) Math.atan2(vy, vx);
        }
    }

    // Getters and setters
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}