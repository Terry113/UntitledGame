package com.github.Terry113;

/**
 * Utility methods for the simulation.
 */
public class Util {
    /**
     * Calculates the squared distance between two points.
     * Using squared distance avoids unnecessary square root calculations.
     * 
     * @param x1 X coordinate of first point
     * @param y1 Y coordinate of first point
     * @param x2 X coordinate of second point
     * @param y2 Y coordinate of second point
     * @return The squared distance between the points
     */
    public static float getDistanceSquared(float x1, float y1, float x2, float y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }
    
    /**
     * Calculates the actual distance between two points.
     * 
     * @param x1 X coordinate of first point
     * @param y1 Y coordinate of first point
     * @param x2 X coordinate of second point
     * @param y2 Y coordinate of second point
     * @return The distance between the points
     */
    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(getDistanceSquared(x1, y1, x2, y2));
    }
    
    /**
     * Limits a value to be within a specified range.
     * 
     * @param value The value to constrain
     * @param min The minimum allowed value
     * @param max The maximum allowed value
     * @return The constrained value
     */
    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}