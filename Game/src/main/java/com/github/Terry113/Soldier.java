package com.github.Terry113;

import java.awt.Color;
import java.awt.Graphics;

public class Soldier extends Unit {
    private int size;
    private State currentState;

    public enum State {
        IDLE,
        SELECTED,
        CHASING,
        SHOOTHING,
        // Add other states as needed
    }

    public Soldier(float x, float y, float vx, float vy, float angle, Color color, int size) {
        super(x, y, vx, vy, angle, color);
        this.size = size;
        currentState = State.IDLE;
    }

    public void detect() {

    }

    @Override
    public void update() {
        switch (currentState) {
            case IDLE:

                break;
            case CHASING:
                steer(0, 0);
                break;
        }
        setX(getX() + getVx());
        setY(getY() + getVy());
    }

    @Override
    public void render(Graphics g) {
        g.setColor(getColor());
        g.fillRect((int) getX() - size / 2, (int) getY() - size / 2, size, size);
    }

}
