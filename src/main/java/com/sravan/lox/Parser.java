package com.sravan.lox;

import static com.sravan.lox.TokenType.*;

import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    private static class ParseError extends RuntimeException {
    };

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    Expr parse() {
        try {
            return expression();
        } catch (ParseError error) {
            return null;
        }
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
        return equality();
    }

    private void consume(TokenType type, String error) {
        if (check(type)) {
            advance();
            return;
        }
        throw error(peek(), error);
    }

    private ParseError error(Token token, String error) {
        Lox.error(token, error);
        return new ParseError();
    }

}
