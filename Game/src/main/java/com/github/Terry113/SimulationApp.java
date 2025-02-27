package com.github.Terry113;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.Timer;

public class SimulationApp {
    public static void main(String[] args) {
        SimulationManager simManager = new SimulationManager(Constants.FIELD_HEIGHT, 600); // Example dimensions
        SimulationRenderer renderer = new SimulationRenderer(simManager);

        JFrame frame = new JFrame("Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(renderer);
        frame.setSize(Constants.FIELD_WIDTH,Constants.FIELD_HEIGHT);
        frame.setVisible(true);

        // Example unit addition
        simManager.addUnit(new Soldier(100, 100, 1, 1, 0, Color.RED, 10));

        // Timer to update and repaint at a fixed rate
        new Timer(16, e -> {
            simManager.update();
            renderer.repaint();
        }).start();
    }
}
