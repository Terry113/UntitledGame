package com.github.Terry113;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class SimulationManager {
    private List<Unit> units;
    private QuadtreeNode quadtree;
    private final int width;
    private final int height;
    private final int quadtreeCapacity = 2; // Will create 4^x amount of chunks

    public SimulationManager(int width, int height) {
        this.width = width;
        this.height = height;
        this.units = new ArrayList<>();
        this.quadtree = new QuadtreeNode(0, 0, width, height, quadtreeCapacity);
    }

    public void addUnit(Unit unit) {
        units.add(unit);
        quadtree.insert(unit);
    }

    public void update() {

        for (Unit unit : units) {
            unit.update();
            
            // Handle boundary conditions and unit interactions here
        }
        
        // Rebuild the quadtree for the next frame to reflect updated positions
        quadtree = new QuadtreeNode(0, 0, width, height, quadtreeCapacity);

        for (Unit unit : units) {
            quadtree.insert(unit);
        }
    }

    public QuadtreeNode getQuadtreeNode(){
        return quadtree;
    }
    public List<Unit> getUnits() {
        return units;
    }
}