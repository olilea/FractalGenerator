package com.oglea;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * @author Oliver Lea
 */
public class Generator extends JPanel {

    public static final int DEFUALT_ITERATIONS = 9;

    private int iterations;

    public Generator() {
        this.iterations = DEFUALT_ITERATIONS;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

    }

    private Color colorForIteration(int iteration) {
        Random r = new Random(iteration);
        return new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }
}
