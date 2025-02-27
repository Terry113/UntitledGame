package com.github.Terry113;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the simulation state including units and spatial partitioning.
 */
public class SimulationManager {
    private List<Unit> units;
    private QuadtreeNode quadtree;
    private final int width;
    private final int height;

    /**
     * Creates a new simulation manager.
     * 
     * @param width Width of the simulation area
     * @param height Height of the simulation area
     */
    public SimulationManager(int width, int height) {
        this.width = width;
        this.height = height;
        this.units = new ArrayList<>();
        this.quadtree = new QuadtreeNode(0, 0, width, height);
    }

    /**
     * Adds a new unit to the simulation.
     * 
     * @param unit The unit to add
     */
    public void addUnit(Unit unit) {
        units.add(unit);
        quadtree.insert(unit);
    }

    /**
     * Updates all units and the spatial partitioning structure.
     */
    public void update() {
        // Update all units
        for (Unit unit : units) {
            // Store old position
            float oldX = unit.getX();
            float oldY = unit.getY();
            
            // Update the unit
            unit.update();
            
            // Handle boundary conditions
            handleBoundaries(unit);
            
            // Only update quadtree if the unit has moved
            if (oldX != unit.getX() || oldY != unit.getY()) {
                updateUnitInQuadtree(unit, oldX, oldY);
            }
        }
        
        // Handle unit interactions
        handleUnitInteractions();
    }
    
    /**
     * Keeps units within the simulation boundaries.
     * 
     * @param unit The unit to check and constrain
     */
    private void handleBoundaries(Unit unit) {
        // Constrain X position
        if (unit.getX() < 0) {
            unit.setX(0);
            unit.setVx(-unit.getVx() * 0.5f); // Bounce with damping
        } else if (unit.getX() > width) {
            unit.setX(width);
            unit.setVx(-unit.getVx() * 0.5f); // Bounce with damping
        }
        
        // Constrain Y position
        if (unit.getY() < 0) {
            unit.setY(0);
            unit.setVy(-unit.getVy() * 0.5f); // Bounce with damping
        } else if (unit.getY() > height) {
            unit.setY(height);
            unit.setVy(-unit.getVy() * 0.5f); // Bounce with damping
        }
    }
    
    /**
     * Updates a unit's position in the quadtree if it has moved.
     * 
     * @param unit The unit to update
     * @param oldX The unit's previous X position
     * @param oldY The unit's previous Y position
     */
    private void updateUnitInQuadtree(Unit unit, float oldX, float oldY) {
        // For simplicity, we'll rebuild the entire quadtree
        // In a more optimized version, we would remove and reinsert only the moved unit
        quadtree.clear();
        for (Unit u : units) {
            quadtree.insert(u);
        }
    }
    
    /**
     * Handles interactions between units.
     */
    private void handleUnitInteractions() {
        // For each unit, find nearby units and handle interactions
        for (Unit unit : units) {
            if (unit instanceof Soldier) {
                Soldier soldier = (Soldier) unit;
                
                // Query for nearby units
                Rectangle queryRange = new Rectangle(
                    (int)(soldier.getX() - Constants.DETECTION_RADIUS),
                    (int)(soldier.getY() - Constants.DETECTION_RADIUS),
                    Constants.DETECTION_RADIUS * 2,
                    Constants.DETECTION_RADIUS * 2
                );
                
                List<Unit> nearbyUnits = new ArrayList<>();
                quadtree.query(queryRange, nearbyUnits);
                
                // Remove self from the list
                nearbyUnits.remove(soldier);
                
                // Let the soldier handle its response to nearby units
                soldier.handleNearbyUnits(nearbyUnits);
            }
        }
    }

    /**
     * @return The quadtree used for spatial partitioning
     */
    public QuadtreeNode getQuadtreeNode() {
        return quadtree;
    }
    
    /**
     * @return The list of all units in the simulation
     */
    public List<Unit> getUnits() {
        return units;
    }
}