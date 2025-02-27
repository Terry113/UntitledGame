package com.github.Terry113;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements a quadtree spatial partitioning data structure for efficient 
 * spatial queries of units in the simulation.
 */
public class QuadtreeNode {
    private int x, y; // Top-left corner of this quadrant
    private int width, height; // Dimensions of this quadrant
    private List<Unit> units; // Units within this quadrant
    private QuadtreeNode[] children; // Child quadrants
    private int maxObjects; // Max units before splitting
    private int maxDepth; // Maximum depth of the tree
    private int depth; // Current depth of this node
    
    /**
     * Creates a new quadtree node.
     * 
     * @param x X coordinate of top-left corner
     * @param y Y coordinate of top-left corner
     * @param width Width of this quadrant
     * @param height Height of this quadrant
     * @param maxObjects Maximum number of objects before splitting
     * @param maxDepth Maximum depth of the tree
     * @param depth Current depth of this node
     */
    public QuadtreeNode(int x, int y, int width, int height, int maxObjects, int maxDepth, int depth) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxObjects = maxObjects;
        this.maxDepth = maxDepth;
        this.depth = depth;
        this.units = new ArrayList<>();
        this.children = null; // Children created only when needed
    }
    
    /**
     * Constructor with default values from Constants.
     * 
     * @param x X coordinate of top-left corner
     * @param y Y coordinate of top-left corner
     * @param width Width of this quadrant
     * @param height Height of this quadrant
     */
    public QuadtreeNode(int x, int y, int width, int height) {
        this(x, y, width, height, Constants.QUADTREE_MAX_OBJECTS, Constants.QUADTREE_MAX_DEPTH, 0);
    }

    /**
     * Subdivides this node into four equal quadrants.
     */
    private void subdivide() {
        int halfWidth = this.width / 2;
        int halfHeight = this.height / 2;
        int nextDepth = this.depth + 1;

        children = new QuadtreeNode[4];
        // Top-left
        children[0] = new QuadtreeNode(x, y, halfWidth, halfHeight, maxObjects, maxDepth, nextDepth);
        // Top-right
        children[1] = new QuadtreeNode(x + halfWidth, y, halfWidth, halfHeight, maxObjects, maxDepth, nextDepth);
        // Bottom-left
        children[2] = new QuadtreeNode(x, y + halfHeight, halfWidth, halfHeight, maxObjects, maxDepth, nextDepth);
        // Bottom-right
        children[3] = new QuadtreeNode(x + halfWidth, y + halfHeight, halfWidth, halfHeight, maxObjects, maxDepth, nextDepth);
    }

    /**
     * Inserts a unit into the quadtree.
     * 
     * @param unit The unit to insert
     * @return true if insertion was successful, false otherwise
     */
    public boolean insert(Unit unit) {
        // If the unit does not belong in this quadrant
        if (!belongsToQuadrant(unit.getX(), unit.getY())) {
            return false;
        }

        // If this node has children, try to insert into them
        if (children != null) {
            for (QuadtreeNode child : children) {
                if (child.insert(unit)) {
                    return true;
                }
            }
            
            // If we get here, the unit couldn't be inserted into any child
            // This can happen at boundaries, so add it to this node
            units.add(unit);
            return true;
        }
        
        // Add unit to this node
        units.add(unit);
        
        // Check if we need to split
        if (units.size() > maxObjects && depth < maxDepth) {
            // Create the children if they don't exist
            if (children == null) {
                subdivide();
            }
            
            // Try to redistribute existing units to children
            List<Unit> remainingUnits = new ArrayList<>();
            for (Unit existingUnit : units) {
                boolean inserted = false;
                for (QuadtreeNode child : children) {
                    if (child.insert(existingUnit)) {
                        inserted = true;
                        break;
                    }
                }
                // If unit couldn't be inserted into any child, keep it in this node
                if (!inserted) {
                    remainingUnits.add(existingUnit);
                }
            }
            
            // Update this node's units to only contain those that couldn't be moved to children
            units.clear();
            units.addAll(remainingUnits);
        }
        
        return true;
    }

    /**
     * Checks if a point belongs to this quadrant.
     * 
     * @param x X coordinate
     * @param y Y coordinate
     * @return true if the point is inside this quadrant
     */
    private boolean belongsToQuadrant(float x, float y) {
        return x >= this.x && y >= this.y && x < this.x + this.width && y < this.y + this.height;
    }

    /**
     * Finds all units within a specified rectangular range.
     * 
     * @param range The rectangular area to query
     * @param found List to store found units (pass an empty list)
     * @return The list of found units
     */
    public List<Unit> query(Rectangle range, List<Unit> found) {
        // Skip this node if it doesn't intersect with the query range
        if (!range.intersects(new Rectangle(this.x, this.y, this.width, this.height))) {
            return found;
        }

        // Add units from this node that are within the range
        for (Unit unit : units) {
            if (range.contains(unit.getX(), unit.getY())) {
                found.add(unit);
            }
        }

        // Check children if they exist
        if (children != null) {
            for (QuadtreeNode child : children) {
                child.query(range, found);
            }
        }

        return found;
    }
    
    /**
     * Clears all units from this quadtree.
     */
    public void clear() {
        units.clear();
        
        if (children != null) {
            for (QuadtreeNode child : children) {
                child.clear();
            }
            children = null; // Remove references to children
        }
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
}