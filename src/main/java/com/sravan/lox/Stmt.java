package com.sravan.lox;

import java.util.List;

abstract class Stmt {

    interface Visitor<R> {
        R visitPrintStmt(Print stmt);

        R visitExpressionStmt(Expression stmt);

        R visitVarStmt(Var stmt);

    }

    static class Print extends Stmt {
        Print(Expr expression) {
            this.expression = expression;
        }

        final Expr expression;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }
    }

    static class Expression extends Stmt {
        Expression(Expr expression) {
            this.expression = expression;
        }

        final Expr expression;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }
    }

    static class Var extends Stmt {
        Var(Token name, Expr initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        final Token name;
        final Expr initializer;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStmt(this);
        }
    }

    abstract <R> R accept(Visitor<R> visitor);
}
