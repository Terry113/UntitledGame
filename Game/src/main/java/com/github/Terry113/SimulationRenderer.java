package com.github.Terry113;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.List;

public class SimulationRenderer extends JPanel {
    private SimulationManager simManager;

    public SimulationRenderer(SimulationManager simManager) {
        this.simManager = simManager;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderSimulation(g);
    }

    private void renderSimulation(Graphics g) {
        List<Unit> units = simManager.getUnits();
        for (Unit unit : units) {
            unit.render(g); // Delegate the rendering to the unit itself
        }
        drawQuadtreeBoundaries(simManager.getQuadtreeNode(), g);
    }

    private void drawQuadtreeBoundaries(QuadtreeNode node, Graphics g) {
        if(node == null){
            return;
        }
        g.drawRect(node.getX(), node.getY(), node.getWidth(), node.getHeight());
        QuadtreeNode[] children = node.getChildren(); // Assuming getChildren() method exists
        if (children != null) {
            for (QuadtreeNode child : children) {
                drawQuadtreeBoundaries(child, g);
            }
        }
    }
}