package com.oglea.fractal;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;

import static java.lang.Math.abs;

/**
 * @author Oliver Lea
 */
public class Mandelbrot extends Fractal {

    private static final Color GRADIENT_START = Color.MAGENTA;
    private static final Color GRADIENT_END = Color.ORANGE;

    private ExecutorService executorService;

    private double visibleAreaXStart = -2.0f;
    private double visibleAreaWidth = 4.0f;
    private double visibleAreaYStart = -2.0f;
    private double visibleAreaHeight = 4.0f;
    private int highestIteration = 1;

    public Mandelbrot() {
        super();
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void draw(int iterations, int width, int height, Graphics2D g) {
        int[][] iterationsMap = new int[height][width];
        highestIteration = 0;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Will get around to using an ExecutorService to execute this in parallel across threads
        BiFunction<Integer, Integer, Integer> mandelbrotFunc = (x, y) -> {
            double r = visibleAreaXStart + x * (visibleAreaWidth / width);
            double i = (visibleAreaYStart + visibleAreaHeight) - y * (visibleAreaHeight / height);
            return mandelbrot(iterations, r, i);
        };

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int iter = mandelbrotFunc.apply(x, y);
                iterationsMap[y][x] = iter;
                if (iter > highestIteration) {
                    highestIteration = iter;
                }
            }
        }
        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int iter = iterationsMap[y][x];
                if (iter < 0) {
                    image.setRGB(x, y, 0x0);
                } else {
                    image.setRGB(x, y, getColor(iter).getRGB());
                }
            }
        }
        g.drawImage(image, 0, 0, width, height, null);
    }

    private int mandelbrot(int iterationCutoff, double realC, double imaginaryC) {
        double zr = 0;
        double zi = 0;
        for (int x = 0; x < iterationCutoff; ++x) {
            double zrSq = zr * zr;
            double ziSq = zi * zi;
            double zrNew = zrSq - ziSq + realC;
            // Add instead of multiplying by two
            zi = (zr + zr) * zi + imaginaryC;
            zr = zrNew;
            if (zrSq + ziSq > 4.0) {
                return x;
            }
        }
        return -1;
    }

    private Color getColor(int iteration) {
        float p = (float) iteration / (float) highestIteration;
        float inverse = 1f - p;
        float[] startColors = GRADIENT_START.getRGBColorComponents(new float[3]);
        float[] endColors = GRADIENT_END.getRGBColorComponents(new float[3]);

        float r = startColors[0] * p + endColors[0] * inverse;
        float g = startColors[1] * p + endColors[1] * inverse;
        float b = startColors[2] * p + endColors[2] * inverse;
        return new Color(r, g, b);
    }

    @Override
    public int getDefaultIterations() {
        return 5;
    }

    @Override
    public void setZoom(int width, int height, Rectangle r) {
        double visibleXStart = this.visibleAreaXStart + (r.getX() * (visibleAreaWidth / width));
        double visibleWidth = abs((this.visibleAreaXStart + (r.getX() + r.getWidth()) * (visibleAreaWidth / width)) - visibleXStart);
        double visibleYStart = this.visibleAreaYStart + (height - (r.getY() + r.getHeight())) * (visibleAreaHeight / height);
        double visibleHeight = abs((visibleAreaYStart + (height - r.getY()) * (visibleAreaHeight / height)) - visibleYStart);

        this.visibleAreaXStart = visibleXStart;
        this.visibleAreaWidth = visibleWidth;
        this.visibleAreaYStart = visibleYStart;
        this.visibleAreaHeight = visibleHeight;
    }

    @Override
    public String toString() {
        return "Mandelbrot";
    }
}
