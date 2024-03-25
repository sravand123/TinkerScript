package com.sravan.lox;

import java.util.List;

abstract class Stmt {

    interface Visitor<R> {
        R visitExpressionStmt(Expression stmt);

        R visitVarStmt(Var stmt);

        R visitBlockStmt(Block stmt);

        R visitIfStmt(If stmt);

        R visitWhileStmt(While stmt);

        R visitFunctionStmt(Function stmt);

        R visitReturnStmt(Return stmt);

        R visitClassStmt(Class stmt);

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

    static class Block extends Stmt {
        Block(List<Stmt> statements) {
            this.statements = statements;
        }

        final List<Stmt> statements;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockStmt(this);
        }
    }

    static class If extends Stmt {
        If(Expr condition , Stmt thenStatement , Stmt elseStatement) {
            this.condition = condition;
            this.thenStatement = thenStatement;
            this.elseStatement = elseStatement;
        }

        final Expr condition;
        final Stmt thenStatement;
        final Stmt elseStatement;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfStmt(this);
        }
    }

    static class While extends Stmt {
        While(Expr condition,  Stmt body) {
            this.condition = condition;
            this.body = body;
        }

        final Expr condition;
        final Stmt body;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitWhileStmt(this);
        }
    }

    static class Function extends Stmt {
        Function(Token name, List<Token> params, Token spread , List<Stmt> body) {
            this.name = name;
            this.params = params;
            this.spread = spread;
            this.body = body;
        }

        final Token name;
        final List<Token> params;
        final Token spread;
        final List<Stmt> body;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitFunctionStmt(this);
        }
    }

    static class Return extends Stmt {
        Return(Token keyword, Expr value) {
            this.keyword = keyword;
            this.value = value;
        }

        final Token keyword;
        final Expr value;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitReturnStmt(this);
        }
    }

    static class Class extends Stmt {
        Class(Token name, List<Stmt.Function> methods, Expr.Variable superClass) {
            this.name = name;
            this.methods = methods;
            this.superClass = superClass;
        }

        final Token name;
        final List<Stmt.Function> methods;
        final Expr.Variable superClass;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitClassStmt(this);
        }
    }

    abstract <R> R accept(Visitor<R> visitor);
}
