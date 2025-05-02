package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UI {

    private static final int WIDTH = 800;
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

        JCheckBox integralBox = new JCheckBox("Show Integral!");
        controlPanel.add(integralBox);

        JCheckBox fillBox = new JCheckBox("Fill integral!");
        controlPanel.add(fillBox);

        JCheckBox yAxisFunctionBox = new JCheckBox("Draw as x = f(y)");
        controlPanel.add(yAxisFunctionBox);


        PlotPanel plotPanel = new PlotPanel();

        inputField.setToolTipText("Use variable x");

        yAxisFunctionBox.addActionListener(e -> {
            plotPanel.setYAxisFunction(yAxisFunctionBox.isSelected());
            inputField.setToolTipText("Use variable " + (yAxisFunctionBox.isSelected() ? "y" : "x"));
            plotPanel.repaint();
        });

        plotButton.addActionListener((ActionEvent e) -> {
            plotPanel.setExpression(inputField.getText());
            plotPanel.repaint();
        });

        integralBox.addActionListener(e -> {
            plotPanel.setShowIntegral(integralBox.isSelected());
            plotPanel.repaint();
        });

        fillBox.addActionListener(e -> {
            plotPanel.setFillIntegral(fillBox.isSelected());
            plotPanel.repaint();
        });

        window.setLayout(new BorderLayout());
        window.add(controlPanel, BorderLayout.NORTH);
        window.add(plotPanel, BorderLayout.CENTER);

        window.setVisible(true);
    }

    static class PlotPanel extends JPanel {
        private java.util.List<String> expressionText = new java.util.ArrayList<>(java.util.List.of("sin(x)"));
        private double scaleX = 40;
        private double scaleY = 40;
        private double offsetX = 0;
        private double offsetY = 0;

        private int mouseX;
        private int mouseY;

        private int lastMouseX, lastMouseY;

        // integrals
        private boolean showIntegral = false;
        private boolean fillIntegral = false;
        private double integralStartX = -5;
        private double integralEndX = 5;

        private boolean isYAxisFunction = false;


        public void setYAxisFunction(boolean value) {
            isYAxisFunction = value;
        }

        public void setShowIntegral(boolean show) {
            this.showIntegral = show;
        }

        public void setFillIntegral(boolean fill) {
            this.fillIntegral = fill;
        }

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

            // Отрисовка сетки
            g.setColor(new Color(200, 200, 200)); // Светло-серый цвет сетки
            double gridSpacing = 1.0; // шаг сетки в мировых координатах

            // Горизонтальные линии
            double yStart = -(getHeight() / 2.0 + offsetY) / scaleY;
            double yEnd = (getHeight() / 2.0 - offsetY) / scaleY;
            for (double y = Math.floor(yStart); y <= yEnd; y += gridSpacing) {
                int py = (int)(getHeight() / 2.0 - y * scaleY + offsetY);
                g.drawLine(0, py, getWidth(), py);
            }

            // Вертикальные линии
            double xStart = -(getWidth() / 2.0 + offsetX) / scaleX;
            double xEnd = (getWidth() / 2.0 - offsetX) / scaleX;
            for (double x = Math.floor(xStart); x <= xEnd; x += gridSpacing) {
                int px = (int)(x * scaleX + getWidth() / 2.0 + offsetX);
                g.drawLine(px, 0, px, getHeight());
            }

            g.setColor(Color.GRAY);
            g.drawLine(0, (int)(getHeight() / 2 + offsetY), getWidth(), (int)(getHeight() / 2 + offsetY));
            g.drawLine((int)(getWidth() / 2 + offsetX), 0, (int)(getWidth() / 2 + offsetX), getHeight());

            for(int f = 0; f < expressionText.size(); ++f) try {
                String rawExpr = expressionText.get(f);
                String variable = isYAxisFunction ? "y" : "x";
                String expr = rawExpr.replaceAll("\\bx\\b", variable).replaceAll("\\by\\b", variable);


                if(showIntegral)  {
                    String expressionWithCorrectVariable = expr.replaceAll("\\bx\\b", variable).replaceAll("\\by\\b", variable);
                    Calculator calculator = new Calculator(expressionWithCorrectVariable);

                    double prevX = -getWidth() / 2.0 / scaleX;
                    double prevY = calculator.Integrate(-10, prevX, 100);

                    g.setColor(new Color(COLORS[f % COLORS.length].getRGB()).darker());
                    g.setBackground(Color.RED);


                    for(int i = 1; i < getWidth(); ++i) {
                        double x = (i - getWidth() / 2.0 - offsetX) / scaleX;
                        double y = calculator.Integrate(-10, x, 100);

                        int x1 = (int) (prevX * scaleX + getWidth() / 2.0 + offsetX);
                        int y1 = (int) (getHeight() / 2.0 - prevY * scaleY + offsetY);
                        int x2 = (int) (x * scaleX + getWidth() / 2.0 + offsetX);
                        int y2 = (int) (getHeight() / 2.0 - y * scaleY + offsetY);

                        g.drawLine(x1, y1, x2, y2);

                        prevX = x;
                        prevY = y;
                    }
                } if (!isYAxisFunction) {
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

                    if (fillIntegral && expressionText.size() >= 2) {
                        try {
                            Calculator top = new Calculator(expressionText.get(0));
                            Calculator bottom = new Calculator(expressionText.get(1));

                            int steps = 200;
                            int[] xPoints = new int[2 * (steps + 1)];
                            int[] yPoints = new int[2 * (steps + 1)];

                            double a = integralStartX;
                            double b = integralEndX;
                            double dx = (b - a) / steps;

                            // Верхняя кривая — слева направо
                            for (int i = 0; i <= steps; i++) {
                                double x = a + i * dx;
                                double y = top.calculate(x);
                                int px = (int)(x * scaleX + getWidth() / 2.0 + offsetX);
                                int py = (int)(getHeight() / 2.0 - y * scaleY + offsetY);

                                xPoints[i] = px;
                                yPoints[i] = py;
                            }

                            // Нижняя кривая — справа налево
                            for (int i = steps; i >= 0; i--) {
                                double x = a + i * dx;
                                double y = bottom.calculate(x);
                                int px = (int)(x * scaleX + getWidth() / 2.0 + offsetX);
                                int py = (int)(getHeight() / 2.0 - y * scaleY + offsetY);

                                xPoints[2 * steps - i + 1] = px;
                                yPoints[2 * steps - i + 1] = py;
                            }

                            g.setColor(new Color(0, 255, 0, 80)); // Полупрозрачный синий
                            g.fillPolygon(xPoints, yPoints, 2 * (steps + 1));

                        } catch (Exception e) {
                            g.setColor(Color.RED);
                            g.drawString("Ошибка заливки между функциями: " + e.getMessage(), 10, getHeight() - 30);
                        }
                    }

                } else {
                    // Новый код для x = f(y)
                    Calculator calculator = new Calculator(expr);
                    double prevY = -getHeight() / 2.0 / scaleY;
                    double prevX = calculator.calculate(prevY);

                    g.setColor(COLORS[f % COLORS.length]);

                    for (int i = 1; i < getHeight(); ++i) {
                        double y = (getHeight() / 2.0 - i - offsetY) / scaleY;
                        double x = calculator.calculate(y);

                        int x1 = (int) (prevX * scaleX + getWidth() / 2.0 + offsetX);
                        int y1 = (int) (getHeight() / 2.0 - prevY * scaleY + offsetY);
                        int x2 = (int) (x * scaleX + getWidth() / 2.0 + offsetX);
                        int y2 = (int) (getHeight() / 2.0 - y * scaleY + offsetY);

                        g.drawLine(x1, y1, x2, y2);

                        prevY = y;
                        prevX = x;
                    }
                }
            } catch (Exception ex) {
                g.setColor(Color.RED);
                g.drawString("Error in function: " + ex.getMessage(), 10, 20 + f * 15);
            }

            g.setColor(Color.RED);

            double xValue = (mouseX - getWidth() / 2.0 - offsetX) / scaleX;
            double yValue = -(mouseY - getHeight() / 2.0f - offsetY) / scaleY;

            String coords = String.format("x = %.2f, y = %.2f", xValue, yValue);
            g.drawString(coords, 10, getHeight() - 10);
        }
    }
}