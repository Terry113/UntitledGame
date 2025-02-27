package com.github.Terry113;

import java.awt.Color;
import java.awt.Graphics;


public abstract class Unit {
    private float x, y; // Position
    private float vx, vy; // Velocity
    private float angle; // Orientation angle in radians
    private Color color;
    private int updateCounter = 0;

    public Unit(float x, float y, float vx, float vy, float angle, Color color) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
        this.color = color;
    }

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

    public abstract void update();

    public abstract void render(Graphics g);

    public void steer(float xTarget, float yTarget) {
        double distanceSquared = Util.getDistanceSquared(xTarget, yTarget, x, y);
        if (distanceSquared > Constants.DETECTION_RADIUS) { // Only steer if the target is not too close
            float steerX = (xTarget - x) / (float) Math.sqrt(distanceSquared) - vx;
            float steerY = (yTarget - y) / (float) Math.sqrt(distanceSquared) - vy;

            // Apply a damping factor to the steering force
            float damping = 0.1f; // Adjust this value as needed
            vx += steerX * damping;
            vy += steerY * damping;

            // Update the angle
            angle = (float) Math.atan2(vy, vx);
        }
    }

}
