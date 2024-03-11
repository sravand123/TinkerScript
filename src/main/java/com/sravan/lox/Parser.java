package com.sravan.lox;

import static com.sravan.lox.TokenType.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    private static class ParseError extends RuntimeException {
    };

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    List<Stmt> parse() {
        try {
            List<Stmt> statements = new ArrayList<>();
            while (!isAtEnd()) {
                statements.add(declaration());
            }
            return statements;
        } catch (ParseError error) {
            return null;
        }
    }

    private Stmt declaration() {
        try {
            if (match(VAR))
                return variableDeclaration();
            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private Stmt variableDeclaration() {
        Token name = consume(IDENTIFIER, "Expected Identifier");
        Expr initializer = null;
        if (match(EQUAL)) {
            initializer = expression();
        }
        consume(SEMICOLON, "Expected ;");
        return new Stmt.Var(name, initializer);
    }

    private Stmt statement() {
        if (match(PRINT))
            return printStatement();
        if (match(LEFT_BRACE)) {
            return new Stmt.Block(block());
        }
        if (match(IF))
            return ifStatement();
        return expressionStatement();

    }

    private Stmt ifStatement() {
        consume(LEFT_PAREN, "Expected )");
        Expr condition = expression();
        consume(RIGHT_PAREN, "Expected )");
        Stmt thenStatement = statement();
        Stmt elseStatement = null;
        if (match(ELSE)) {
            elseStatement = statement();
        }
        return new Stmt.If(condition, thenStatement, elseStatement);
    }

    private List<Stmt> block() {
        List<Stmt> statements = new ArrayList<>();
        while (!isAtEnd() && !check(RIGHT_BRACE)) {
            statements.add(declaration());
        }
        consume(RIGHT_BRACE, "Expected }");
        return statements;
    }

    private Stmt printStatement() {
        Expr expr = expression();
        consume(SEMICOLON, "Expected ;");
        return new Stmt.Print(expr);
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        consume(SEMICOLON, "Expected ;");
        return new Stmt.Expression(expr);
    }

    private boolean match(TokenType... types) {
        for (TokenType tokenType : types) {
            if (check(tokenType)) {
                advance();
                return true;
            }
        }
        return false;

    }

    private Token advance() {
        if (!isAtEnd())
            current++;
        return previous();
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean check(TokenType type) {
        if (isAtEnd())
            return false;
        return peek().type == type;
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Expr primary() {
        if (match(FALSE))
            return new Expr.Literal(false);
        if (match(TRUE))
            return new Expr.Literal(true);

        if (match(NIL))
            return new Expr.Literal(null);
        if (match(STRING, NUMBER))
            return new Expr.Literal(previous().literal);
        if (match(LEFT_PAREN)) {
            Expr expression = expression();
            consume(RIGHT_PAREN, "expected ) after expression.");
            return new Expr.Grouping(expression);
        }
        if (match(IDENTIFIER))
            return new Expr.Variable(previous());

        throw error(peek(), "expected expression.");

    }

    private Expr unary() {
        if (match(BANG, MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }
        return primary();
    }

    private Expr factor() {
        Expr expr = unary();
        while (match(STAR, SLASH)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr term() {
        Expr expr = factor();
        while (match(MINUS, PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr comparison() {
        Expr expr = term();
        while (match(LESS_EQUAL, LESS, GREATER, GREATER_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr equality() {
        Expr expr = comparison();
        while (match(EQUAL_EQUAL, BANG_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expr = new Expr.Binary(expr, operator, right);
        }
        return expr;
    }

    private Expr expression() {
        return assignment();
    }

    private Expr or() {
        Expr expr = and();
        while (match(OR)) {
            Token name = previous();
            Expr right = and();
            expr = new Expr.Logical(expr, name, right);
        }
        return expr;
    }

    private Expr and() {
        Expr expr = equality();
        while (match(AND)) {
            Token name = previous();
            Expr right = equality();
            expr = new Expr.Logical(expr, name, right);
        }
        return expr;
    }
    private Expr assignment() {
        Expr expr = or();
        if (match(EQUAL)) {
            Token equals = previous();
            Expr right = assignment();

            if (expr instanceof Expr.Variable) {
                return new Expr.Assign(((Expr.Variable) expr).name, right);
            }
            error(equals, "Invalid assignment target");
        }
        return expr;
    }

    private Token consume(TokenType type, String error) {
        if (check(type)) {
            advance();
            return previous();
        }
        throw error(peek(), error);
    }

    private ParseError error(Token token, String error) {
        Lox.error(token, error);
        return new ParseError();
    }

    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().type == SEMICOLON)
                return;
            switch (peek().type) {
                case CLASS:
                case FUN:
                case FOR:
                case VAR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;

                default:
                    break;
            }
            advance();
        }
    }
}
