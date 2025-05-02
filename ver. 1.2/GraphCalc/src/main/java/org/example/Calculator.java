package org.example;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;


public class Calculator {

    private final Expression expression;

    public Calculator(String expr) {
        this.expression = new ExpressionBuilder(expr)
                .variable("x")
                .build();
    }

    public double calculate(double x) {
        return expression.setVariable("x", x).evaluate();
    }

    public double Integrate(double a, double b, int steps) {
        double h = (b - a) / steps;
        double sum = 0.5 * (calculate(a) + calculate(b));
        for (int i = 1; i < steps; i++) {
            double x = a + i * h;
            sum += calculate(x);
        }
        return sum * h;
    }
}
