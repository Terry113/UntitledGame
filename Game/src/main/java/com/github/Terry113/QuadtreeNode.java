package com.github.Terry113;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class QuadtreeNode {
    private int x, y; // Top-left corner of this quadrant
    private int width, height; // Dimensions of this quadrant
    private List<Unit> units; // Units within this quadrant
    private QuadtreeNode[] children; // Child quadrants, always initialized
    private int capacity; // Max units before delegation to children

    public QuadtreeNode(int x, int y, int width, int height, int capacity) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.capacity = capacity;
        this.units = new ArrayList<>();
        this.children = null; // Initialized as null, to be created on construction if needed

        // Automatically subdivide if the capacity is greater than 0
        if (capacity > 0) {
            subdivide();
        }
    }

    private void subdivide() {
        int halfWidth = this.width / 2;
        int halfHeight = this.height / 2;

        children = new QuadtreeNode[4];
        children[0] = new QuadtreeNode(x, y, halfWidth, halfHeight, capacity-1); // Capacity set to 0 for leaf nodes
        children[1] = new QuadtreeNode(x + halfWidth, y, halfWidth, halfHeight, capacity-1);
        children[2] = new QuadtreeNode(x, y + halfHeight, halfWidth, halfHeight, capacity-1);
        children[3] = new QuadtreeNode(x + halfWidth, y + halfHeight, halfWidth, halfHeight, capacity-1);
    }

    public boolean insert(Unit unit) {
        // If the unit does not belong in this quadrant
        if (!belongsToQuadrant(unit.getX(), unit.getY())) {
            return false;
        }

        // Delegate to children if they exist
        if (children != null) {
            for (QuadtreeNode child : children) {
                if (child.insert(unit)) {
                    return true;
                }
            }
            // If we cannot insert into children (which shouldn't happen with correct boundary checks), return false
            return false;
        } else {
            // Insert directly if this is a leaf node
            if (capacity == 0) {
                units.add(unit);
                return true;
            } else {
                // This node is full and cannot accept more units
                return false;
            }
        }
    }

    private boolean belongsToQuadrant(float x, float y) {
        return x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
    }

    public List<Unit> query(Rectangle range, List<Unit> found) {
        if (!range.intersects(new Rectangle(this.x, this.y, this.width, this.height))) {
            return found; // Empty list if the range does not intersect this quadrant
        }

        for (Unit unit : units) {
            if (range.contains(unit.getX(), unit.getY())) {
                found.add(unit);
            }
        }

        if (children != null) {
            for (QuadtreeNode child : children) {
                child.query(range, found);
            }
        }

        return found;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Unit> getUnits() {
        return units;
    }

    public QuadtreeNode[] getChildren() {
        return children;
    }
    
    // Additional methods as needed...
}
