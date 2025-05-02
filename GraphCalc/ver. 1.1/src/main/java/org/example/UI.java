package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.Expression;

import static java.awt.Color.BLUE;

public class UI {

    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

    // Colors
    private static final Color[] COLORS = {
            Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.PINK, Color.CYAN
    };


    public static void CreateWindow(){
        JFrame window = new JFrame("Calculator");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setSize(WIDTH, HEIGHT);

        JTextField inputField = new JTextField("sin(x)", 30);
        JButton plotButton = new JButton("Create");

        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Function"));
        controlPanel.add(inputField);
        controlPanel.add(plotButton);

        PlotPanel plotPanel = new PlotPanel();

        plotButton.addActionListener((ActionEvent e) -> {
            plotPanel.setExpression(inputField.getText());
            plotPanel.repaint();
        });

        window.setLayout(new BorderLayout());
        window.add(controlPanel, BorderLayout.NORTH);
        window.add(plotPanel, BorderLayout.CENTER);

        window.setVisible(true);
    }

    static class PlotPanel extends JPanel {
        //private String expressionText = "sin(x)";
        private java.util.List<String> expressionText = new java.util.ArrayList<>(java.util.List.of("sin(x)"));
        private double scaleX = 40;
        private double scaleY = 40;
        private double offsetX = 0;
        private double offsetY = 0;

        private int mouseX;
        private int mouseY;

        private int lastMouseX, lastMouseY;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawFunction((Graphics2D) g);
        }

        public PlotPanel() {
            addMouseWheelListener(e -> {
                double zoomFactor = 1.1;
                if (e.getPreciseWheelRotation() < 0) {
                    scaleX *= zoomFactor;
                    scaleY *= zoomFactor;
                } else {
                    scaleX /= zoomFactor;
                    scaleY /= zoomFactor;
                }
                repaint();
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    lastMouseX = e.getX();
                    lastMouseY = e.getY();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    int dx = e.getX() - lastMouseX;
                    int dy = e.getY() - lastMouseY;

                    offsetX += dx;
                    offsetY += dy;

                    lastMouseX = e.getX();
                    lastMouseY = e.getY();

                    repaint();
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    mouseX = e.getX();
                    mouseY = e.getY();
                    repaint();
                }
            });


        }

        void setExpression(String expr) {
           expressionText.clear();
           for(String part : expr.split(",")) {
               String trimed = part.trim();
               if(!trimed.isEmpty()){
                   expressionText.add(trimed);
               }
           }
        }

        private void drawFunction(Graphics2D g) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.GRAY);
            g.drawLine(0, (int)(getHeight() / 2 + offsetY), getWidth(), (int)(getHeight() / 2 + offsetY));
            g.drawLine((int)(getWidth() / 2 + offsetX), 0, (int)(getWidth() / 2 + offsetX), getHeight());

            for(int f = 0; f < expressionText.size(); ++f) {
                String expr = expressionText.get(f);
                try {
                    Calculator calculator = new Calculator(expr);
                    double prevX = -getWidth() / 2.0 / scaleX;
                    double prevY = calculator.calculate(prevX);

                    g.setColor(COLORS[f % COLORS.length]);

                    for(int i = 1; i < getWidth(); ++i) {
                        double x = (i - getWidth() / 2.0 - offsetX) / scaleX;
                        double y = calculator.calculate(x);

                        int x1 = (int) (prevX * scaleX + getWidth() / 2.0 + offsetX);
                        int y1 = (int) (getHeight() / 2.0 - prevY * scaleY + offsetY);
                        int x2 = (int) (x * scaleX + getWidth() / 2.0 + offsetX);
                        int y2 = (int) (getHeight() / 2.0 - y * scaleY + offsetY);

                        g.drawLine(x1, y1, x2, y2);

                        prevX = x;
                        prevY = y;
                    }
                } catch (Exception ex) {
                    g.setColor(Color.RED);
                    g.drawString("Error in function: " + expr, 10, 20 + f * 15);
                }
            }

            g.setColor(Color.RED);

            double xValue = (mouseX - getWidth() / 2.0 - offsetX) / scaleX;
            double yValue = -(mouseY - getHeight() / 2.0f - offsetY) / scaleY;

            String coords = String.format("x = %.2f, y = %.2f", xValue, yValue);
            g.drawString(coords, 10, getHeight() - 10);
        }
    }
}
