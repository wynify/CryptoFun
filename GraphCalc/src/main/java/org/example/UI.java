package org.example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static java.awt.Color.BLUE;

public class UI {

    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;

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
        private String expressionText = "sin(x)";

        void setExpression(String expr) {
            this.expressionText = expr;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawFunction((Graphics2D) g);
        }

        private void drawFunction(Graphics2D g) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.RED);
            g.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2);
            g.drawLine(getWidth() / 2 , 0, getWidth() / 2, getHeight());

            try {
                Calculator calculator = new Calculator(expressionText);

                double scaleX = 40;
                double scaleY = 40;
                double prevX = -getWidth() / 2.0 / scaleX;
                double prevY = calculator.calculate(prevX);

                g.setColor(BLUE);
                for(int i = 1; i < getWidth(); ++i) {
                    double x = (i - getWidth() / 2.0) / scaleX;
                    double y = calculator.calculate(x);

                    int x1 = (int) (prevX * scaleX + getWidth() / 2.0);
                    int y1 = (int) (getHeight() / 2.0 - prevY * scaleY);
                    int x2 = (int) (x * scaleX + getWidth() / 2.0);
                    int y2 = (int) (getHeight() / 2.0 - y * scaleY);

                    g.drawLine(x1, y1, x2, y2);

                    prevX = x;
                    prevY = y;
                }
            } catch (Exception ex) {
                g.setColor(BLUE);
                g.drawString("Error" + ex.getMessage(), 10, 20);
            }
        }
    }
}
