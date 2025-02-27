package com.github.Terry113;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Color;
import java.util.List;

/**
 * Handles the rendering of the simulation to the screen.
 */
public class SimulationRenderer extends JPanel {
    private SimulationManager simManager;
    private boolean showQuadtree = true; // Toggle to show/hide quadtree boundaries

    /**
     * Creates a new renderer for the given simulation manager.
     * 
     * @param simManager The simulation manager to render
     */
    public SimulationRenderer(SimulationManager simManager) {
        this.simManager = simManager;
        setBackground(Color.WHITE); // Set background color
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Draw quadtree boundaries if enabled
        if (showQuadtree) {
            g.setColor(Color.LIGHT_GRAY);
            drawQuadtreeBoundaries(simManager.getQuadtreeNode(), g);
        }
        
        // Draw all units
        renderSimulation(g);
    }

    /**
     * Renders all units in the simulation.
     * 
     * @param g The graphics context to render to
     */
    private void renderSimulation(Graphics g) {
        List<Unit> units = simManager.getUnits();
        for (Unit unit : units) {
            unit.render(g); // Delegate the rendering to the unit itself
        }
    }

    /**
     * Recursively draws the boundaries of the quadtree.
     * 
     * @param node The quadtree node to draw
     * @param g The graphics context to render to
     */
    private void drawQuadtreeBoundaries(QuadtreeNode node, Graphics g) {
        if (node == null) {
            return;
        }
        
        g.drawRect(node.getX(), node.getY(), node.getWidth(), node.getHeight());
        
        QuadtreeNode[] children = node.getChildren();
        if (children != null) {
            for (QuadtreeNode child : children) {
                drawQuadtreeBoundaries(child, g);
            }
        }
    }
    
    /**
     * Toggles the display of quadtree boundaries.
     */
    public void toggleQuadtreeDisplay() {
        showQuadtree = !showQuadtree;
        repaint();
    }
}