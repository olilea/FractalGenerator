package com.oglea;

import com.oglea.fractal.SierpinskiTriangle;
import com.oglea.fractal.Fractal;
import com.oglea.fractal.Mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

/**
 * @author Oliver Lea
 */
public class FractalGenerator extends JPanel {

    private static final int MINIMUM_WIDTH = 800;
    private static final int MINIMUM_HEIGHT= 500;

    private FractalDrawingPanel drawingPanel;
    private JComboBox<Fractal> fractalCombo;
    private JSpinner iterationSpinner;

    public FractalGenerator() {
        initComponents();
        layoutComponents();
    }

    private void initComponents() {
        fractalCombo = new JComboBox<>();
        fractalCombo.addItem(new Mandelbrot());
        fractalCombo.addItem(new SierpinskiTriangle());

        fractalCombo.addActionListener(ae -> updateGui());

        iterationSpinner = new JSpinner(new SpinnerNumberModel(
                ((Fractal) fractalCombo.getSelectedItem()).getDefaultIterations(), 1, 10000, 1
        ));
        iterationSpinner.addChangeListener(ce -> updateGui());

        drawingPanel = new FractalDrawingPanel();
        drawingPanel.setFractal(new Mandelbrot());
        drawingPanel.setIterations(5);
    }

    private void updateGui() {
        Fractal selectedFractal = ((Fractal) fractalCombo.getSelectedItem());
        if (selectedFractal == drawingPanel.getFractal()) {
            drawingPanel.setIterations(((int) iterationSpinner.getValue()));
        } else {
            int defaultIterations = selectedFractal.getDefaultIterations();
            iterationSpinner.setValue(defaultIterations);
            drawingPanel.setIterations(defaultIterations);
        }
        drawingPanel.setFractal(((Fractal) fractalCombo.getSelectedItem()));
        drawingPanel.repaint();
    }

    private void layoutComponents() {
        setLayout(new BorderLayout());
        add(drawingPanel, BorderLayout.CENTER);
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        controlPanel.add(fractalCombo);
        controlPanel.add(iterationSpinner);
        add(controlPanel, BorderLayout.PAGE_END);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Fractal Generator");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.add(new FractalGenerator());
            frame.setMinimumSize(new Dimension(MINIMUM_WIDTH, MINIMUM_HEIGHT));
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private class FractalDrawingPanel extends JPanel {

        private Fractal fractal;
        private int iterations;

        private Optional<Point> dragStartPoint = Optional.empty();
        private Optional<Point> dragCurrentPoint = Optional.empty();

        public FractalDrawingPanel() {
            super.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    int x = e.getX();
                    int y = e.getY();
                    dragStartPoint = Optional.of(new Point(x, y));
                    dragCurrentPoint = Optional.of(new Point(x, y));
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    dragStartPoint.ifPresent(start -> {
                        Rectangle r = new Rectangle(start);
                        r.add(dragCurrentPoint.get());
                        fractal.setZoom(getWidth(), getHeight(), r);
                    });
                    dragStartPoint = Optional.empty();
                    dragCurrentPoint = Optional.empty();
                    repaint();
                }
            });
            super.addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    super.mouseDragged(e);
                    Point dStart = dragStartPoint.get();
                    int dx = Math.abs(dStart.x - e.getX());
                    int dy = Math.abs(dStart.y - e.getY());
                    int min = Math.min(dx, dy);
                    double aspectRatio = (double) getWidth() / (double) getHeight();
                    dragCurrentPoint = Optional.of(new Point(dStart.x + (int) (min * aspectRatio), dStart.y + min));
                    repaint();
                }
            });
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            this.fractal.draw(iterations, getWidth(), getHeight(), ((Graphics2D) g));

            dragStartPoint.ifPresent(start -> {
                Rectangle r = new Rectangle(start);
                r.add(dragCurrentPoint.get());
                g.setColor(Color.WHITE);
                g.drawRect(r.x, r.y, r.width, r.height);
            });
        }

        public Fractal getFractal() {
            return this.fractal;
        }

        public void setFractal(Fractal fractal) {
            this.fractal = fractal;
        }

        public void setIterations(int iterations) {
            this.iterations = iterations;
        }
    }
}
