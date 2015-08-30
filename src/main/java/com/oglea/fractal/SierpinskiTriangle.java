package com.oglea.fractal;

import com.oglea.Triplet;

import java.awt.*;

/**
 * @author Oliver Lea
 */
public class SierpinskiTriangle extends Fractal {

    public SierpinskiTriangle() {
        super();
    }

    public void draw(int iterations, int width, int height, Graphics2D g) {
        Point left = new Point(width / 10, (height / 10) * 9);
        Point top = new Point(width / 2, height / 10);
        Point right = new Point((width / 10) * 9, (height / 10) * 9);

        iterate(iterations, g, new Triplet<>(left, top, right));
    }

    private void iterate(int iterationsRemaining, Graphics2D g, Triplet<Point> triangle) {
        --iterationsRemaining;
        if (iterationsRemaining == 0) {
            drawTriangle(g, triangle);
            return;
        }

        Point left = triangle.getFirst();
        Point top = triangle.getSecond();
        Point right = triangle.getThird();

        int midY = (left.y + top.y) / 2;
        int midX = (left.x + right.x) / 2;

        // Left
        iterate(iterationsRemaining, g, new Triplet<>(
                left,
                new Point((left.x + top.x) / 2, midY),
                new Point(midX, left.y)
        ));
        iterate(iterationsRemaining, g, new Triplet<>(
                new Point((left.x + top.x) / 2, midY),
                top,
                new Point((top.x + right.x) / 2, midY)
        ));
        // Right
        iterate(iterationsRemaining, g, new Triplet<>(
                new Point(midX, right.y),
                new Point((right.x + top.x) / 2, midY),
                right
        ));
    }

    private void drawTriangle(Graphics g, Triplet<Point> triangle) {
        Point first = triangle.getFirst();
        Point second = triangle.getSecond();
        Point third = triangle.getThird();
        int[] xs = new int[] {
                first.x,
                second.x,
                third.x
        };
        int[] ys = new int[] {
                first.y,
                second.y,
                third.y
        };
        g.setColor(Color.DARK_GRAY);
        g.fillPolygon(xs, ys, 3);
    }

    @Override
    public int getDefaultIterations() {
        return 7;
    }

    @Override
    public void setZoom(int width, int height, Rectangle r) {

    }
}
