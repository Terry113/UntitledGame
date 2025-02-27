package com.github.Terry113;

public class Util {
    public static float getDistanceSquared(float xTarget, float yTarget, float x, float y) {
        return (xTarget - x) * (xTarget - x) + (yTarget - y) * (yTarget - y);
    }
}
