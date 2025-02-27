package com.github.Terry113;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.Random;

/**
 * Represents a soldier unit in the simulation.
 */
public class Soldier extends Unit {
    private int size;
    private State currentState;
    private Random random = new Random();
    private float targetX, targetY; // Target position for movement
    private static final float MAX_SPEED = 2.0f;
    private int stateTimer = 0; // Timer for state transitions
    
    /**
     * Possible states for the soldier.
     */
    public enum State {
        IDLE,       // Not moving
        WANDERING,  // Moving randomly
        CHASING,    // Moving toward another unit
        FLEEING     // Moving away from another unit
    }

    /**
     * Creates a new soldier.
     * 
     * @param x Initial X position
     * @param y Initial Y position
     * @param vx Initial X velocity
     * @param vy Initial Y velocity
     * @param angle Initial angle
     * @param color Color of the soldier
     * @param size Size of the soldier
     */
    public Soldier(float x, float y, float vx, float vy, float angle, Color color, int size) {
        super(x, y, vx, vy, angle, color);
        this.size = size;
        this.currentState = State.IDLE;
        
        // Initialize with random movement target
        setRandomTarget();
    }
    
    /**
     * Sets a random target position for the soldier to move toward.
     */
    private void setRandomTarget() {
        targetX = random.nextInt(Constants.FIELD_WIDTH);
        targetY = random.nextInt(Constants.FIELD_HEIGHT);
    }

    /**
     * Handles the soldier's response to nearby units.
     * 
     * @param nearbyUnits List of units within detection radius
     */
    public void handleNearbyUnits(List<Unit> nearbyUnits) {
        if (nearbyUnits.isEmpty()) {
            // No units nearby, transition to wandering if not already
            if (currentState != State.WANDERING && currentState != State.IDLE) {
                currentState = State.WANDERING;
                setRandomTarget();
            }
            return;
        }
        
        // Find the closest unit
        Unit closest = null;
        float closestDistSquared = Float.MAX_VALUE;
        
        for (Unit other : nearbyUnits) {
            float distSquared = Util.getDistanceSquared(other.getX(), other.getY(), getX(), getY());
            if (distSquared < closestDistSquared) {
                closestDistSquared = distSquared;
                closest = other;
            }
        }
        
        // If there's a unit nearby, chase or flee based on color
        if (closest != null) {
            if (getColor().equals(closest.getColor())) {
                // Same color, follow
                currentState = State.CHASING;
                targetX = closest.getX();
                targetY = closest.getY();
            } else {
                // Different color, flee
                currentState = State.FLEEING;
                // Calculate direction away from other unit
                float dx = getX() - closest.getX();
                float dy = getY() - closest.getY();
                // Normalize and set target position
                float length = (float) Math.sqrt(dx * dx + dy * dy);
                if (length > 0) {
                    dx /= length;
                    dy /= length;
                    targetX = getX() + dx * 100; // Move 100 units away
                    targetY = getY() + dy * 100;
                }
            }
        }
    }

    @Override
    public void update() {
        stateTimer++;
        
        // Potentially change state randomly
        if (stateTimer > 100 && random.nextFloat() < 0.05f) {
            stateTimer = 0;
            
            // 20% chance to transition to idle, 80% to wandering
            if (random.nextFloat() < 0.2f) {
                currentState = State.IDLE;
                setVx(0);
                setVy(0);
            } else {
                currentState = State.WANDERING;
                setRandomTarget();
            }
        }
        
        switch (currentState) {
            case IDLE:
                // Gradually slow down
                setVx(getVx() * 0.9f);
                setVy(getVy() * 0.9f);
                break;
                
            case WANDERING:
            case CHASING:
                // Move toward target
                steer(targetX, targetY);
                // If we reached the target, set a new one
                if (Util.getDistanceSquared(targetX, targetY, getX(), getY()) < 100) {
                    if (currentState == State.WANDERING) {
                        setRandomTarget();
                    }
                }
                break;
                
            case FLEEING:
                // Move toward flee target
                steer(targetX, targetY);
                // After moving for a bit, transition back to wandering
                if (stateTimer > 50) {
                    currentState = State.WANDERING;
                    setRandomTarget();
                }
                break;
        }
        
        // Limit speed
        float speed = (float) Math.sqrt(getVx() * getVx() + getVy() * getVy());
        if (speed > MAX_SPEED) {
            setVx(getVx() * MAX_SPEED / speed);
            setVy(getVy() * MAX_SPEED / speed);
        }
        
        // Update position
        setX(getX() + getVx());
        setY(getY() + getVy());
        
        // Update angle to match movement direction
        if (getVx() != 0 || getVy() != 0) {
            setAngle((float) Math.atan2(getVy(), getVx()));
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(getColor());
        
        // Draw the soldier as a filled rectangle
        g.fillRect((int) getX() - size / 2, (int) getY() - size / 2, size, size);
        
        // Draw a line indicating the facing direction
        float lineLength = size * 0.8f;
        float endX = getX() + (float) Math.cos(getAngle()) * lineLength;
        float endY = getY() + (float) Math.sin(getAngle()) * lineLength;
        g.drawLine((int) getX(), (int) getY(), (int) endX, (int) endY);
        
        // Debug: Draw different outline based on state
        switch (currentState) {
            case IDLE:
                g.setColor(Color.BLACK);
                break;
            case WANDERING:
                g.setColor(Color.GRAY);
                break;
            case CHASING:
                g.setColor(Color.GREEN);
                break;
            case FLEEING:
                g.setColor(Color.RED);
                break;
        }
        
        g.drawRect((int) getX() - size / 2, (int) getY() - size / 2, size, size);
    }

    public State getCurrentState() {
        return currentState;
    }
    
    public void setState(State state) {
        this.currentState = state;
    }
}