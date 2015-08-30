package com.oglea.fractal;

import java.awt.*;

/**
 * @author Oliver Lea
 */
public abstract class Fractal {

    protected Fractal() {

    }

    public abstract void draw(int iterations, int width, int height, Graphics2D g);

    public abstract int getDefaultIterations();

    public abstract void setZoom(int width, int height, Rectangle r);
}
