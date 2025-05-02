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
}
