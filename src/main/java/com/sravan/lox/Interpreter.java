package com.sravan.lox;

import java.util.List;

import com.sravan.lox.Expr.Binary;
import com.sravan.lox.Expr.Grouping;
import com.sravan.lox.Expr.Literal;
import com.sravan.lox.Expr.Unary;
import com.sravan.lox.Stmt.Expression;
import com.sravan.lox.Stmt.Print;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
    void interpret(List<Stmt> statements) {
        try {
            for (Stmt stmt : statements) {
                execute(stmt);
            }
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    void execute(Stmt statement) {
        statement.accept(this);
    }

    @Override
    public Object visitBinaryExpr(Binary expr) {
        Object leftVal = evaluate(expr.left);
        Object rightVal = evaluate(expr.right);
        switch (expr.operator.type) {
            case STAR:
                checkNumberOperands(expr.operator, leftVal, rightVal);
                return (double) leftVal * (double) rightVal;
            case SLASH:
                checkNumberOperands(expr.operator, leftVal, rightVal);
                return (double) leftVal / (double) rightVal;
            case MINUS:
                checkNumberOperands(expr.operator, leftVal, rightVal);
                return (double) leftVal - (double) rightVal;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, leftVal, rightVal);
                return (double) leftVal <= (double) rightVal;
            case LESS:
                checkNumberOperands(expr.operator, leftVal, rightVal);
                return (double) leftVal < (double) rightVal;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, leftVal, rightVal);
                return (double) leftVal >= (double) rightVal;
            case GREATER:
                checkNumberOperands(expr.operator, leftVal, rightVal);
                return (double) leftVal > (double) rightVal;
            case EQUAL_EQUAL:
                return isEqual(leftVal, rightVal);
            case BANG_EQUAL:
                return !isEqual(leftVal, rightVal);
            case PLUS:
                if (leftVal instanceof Double && rightVal instanceof Double) {
                    return (double) leftVal + (double) rightVal;
                }
                if (leftVal instanceof String && rightVal instanceof String) {
                    return (String) leftVal + (String) rightVal;
                }
                throw new RuntimeError(expr.operator, "Operands must be two numbers or two string");
            default:
                return null;
        }
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        return (evaluate(expr.expression));

    }

    @Override
    public Object visitLiteralExpr(Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpr(Unary expr) {
        Object right = evaluate(expr.right);
        switch (expr.operator.type) {
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double) right;
            case BANG:
                return !isTruthy(right);
            default:
                return null;

        }
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean isTruthy(Object value) {
        if (value == null)
            return false;
        if (value instanceof Boolean)
            return (boolean) value;
        return true;

    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null)
            return true;
        if (a == null)
            return false;
        return a.equals(b);
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double)
            return;
        throw new RuntimeError(operator, "Operands must be numbers");
    }

    private void checkNumberOperand(Token operator, Object right) {
        if (right instanceof Double)
            return;
        throw new RuntimeError(operator, "Operand must be a number");
    }

    private String stringify(Object value) {
        if (value == null)
            return "nil";
        String text = value.toString();
        if (value instanceof Double) {
            if (text.endsWith(".0")) {
                return text.substring(0, text.length() - 2);
            }
            return text;
        }
        return text;
    }

    @Override
    public Void visitPrintStmt(Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visitExpressionStmt(Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }
}
