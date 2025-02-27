package com.github.Terry113;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Main application class that sets up the simulation and rendering.
 */
public class SimulationApp {
    public static void main(String[] args) {
        // Create simulation manager with dimensions from constants
        SimulationManager simManager = new SimulationManager(
            Constants.FIELD_WIDTH, 
            Constants.FIELD_HEIGHT
        );
        
        // Create renderer
        SimulationRenderer renderer = new SimulationRenderer(simManager);

        // Set up the main window
        JFrame frame = new JFrame("Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(renderer);
        frame.setSize(Constants.FIELD_WIDTH, Constants.FIELD_HEIGHT);
        frame.setVisible(true);

        // Add some initial units to the simulation
        simManager.addUnit(new Soldier(100, 100, 1, 1, 0, Color.RED, 10));
        simManager.addUnit(new Soldier(300, 200, -1, 0.5f, 0, Color.BLUE, 10));
        simManager.addUnit(new Soldier(500, 400, 0, -1, 0, Color.GREEN, 10));
        simManager.addUnit(new Soldier(450, 400, 0, -1, 0, Color.GREEN, 10));

        // Timer to update and repaint at approximately 60 FPS
        new Timer(16, e -> {
            simManager.update();
            renderer.repaint();
        }).start();
    }
}