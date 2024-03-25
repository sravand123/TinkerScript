package com.sravan.lox;

import static com.sravan.lox.TokenType.*;

import java.util.ArrayList;
import java.util.Arrays;
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
            if (checkVariableDeclaration()) {
                match(VAR);
                return variableDeclaration();
            }
            if (match(FUN))
                return function("function");
            if (match(CLASS))
                return classDeclaration();
            return statement();
        } catch (ParseError error) {
            synchronize();
            return null;
        }
    }

    private Stmt classDeclaration() {
        Token name = consume(IDENTIFIER, "Expected class name.");
        Expr.Variable superClass = null;
        if (match(LESS)) {
            superClass = new Expr.Variable(consume(IDENTIFIER, "Expected super class name"));
        }
        consume(LEFT_BRACE, "Expected {");
        List<Stmt.Function> methods = new ArrayList<>();
        while (!isAtEnd() && !check(RIGHT_BRACE)) {
            methods.add(function("method"));
        }
        consume(RIGHT_BRACE, "Expected } after class body");
        return new Stmt.Class(name, methods, superClass);

    }

    private Stmt variableDeclaration() {
        Token name = consume(IDENTIFIER, "Expected Identifier");
        Expr initializer = null;
        if (match(EQUAL) || match(COLON_EQUAL)) {
            initializer = expression();
        }
        consume(SEMICOLON, "Expected ;");
        return new Stmt.Var(name, initializer);
    }

    private Stmt statement() {
        if (match(LEFT_BRACE)) {
            return new Stmt.Block(block());
        }
        if (match(WHILE)) {
            return whileStatement();
        }
        if (match(FOR))
            return forLoop();

        if (match(IF))
            return ifStatement();
        if (match(RETURN))
            return returnStatement();
        return expressionStatement();

    }

    private Stmt returnStatement() {
        Expr value = null;
        Token keyword = previous();
        if (!check(SEMICOLON)) {
            value = expression();
        }
        consume(SEMICOLON, "Expected ;");
        return new Stmt.Return(keyword, value);
    }

    private Stmt forLoop() {
        consume(LEFT_PAREN, "Expected (");
        Stmt initializer;
        if (match(SEMICOLON)) {
            initializer = null;
        } else if (checkVariableDeclaration()) {
            match(VAR);
            initializer = variableDeclaration();
        } else {
            initializer = expressionStatement();
        }

        Expr condition = null;

        if (!check(SEMICOLON)) {
            condition = expression();
        }
        consume(SEMICOLON, "Expected ;");

        Expr increment = null;

        if (!check(RIGHT_PAREN)) {
            increment = expression();
        }
        consume(RIGHT_PAREN, "Expected )");
        Stmt body = statement();

        if (increment != null) {
            body = new Stmt.Block(Arrays.asList(body, new Stmt.Expression(increment)));
        }

        if (condition == null)
            condition = new Expr.Literal(true);
        body = new Stmt.While(condition, body);

        if (initializer != null) {
            body = new Stmt.Block(Arrays.asList(initializer, body));
        }
        return body;
    }

    private Stmt whileStatement() {
        consume(LEFT_PAREN, "Expected (");
        Expr condition = expression();
        consume(RIGHT_PAREN, "Expected )");
        Stmt body = statement();
        return new Stmt.While(condition, body);
    }

    private Stmt ifStatement() {
        consume(LEFT_PAREN, "Expected (");
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

    private boolean check(TokenType... type) {
        for (TokenType tokenType : type) {
            if (isAtEnd())
                return false;
            if (peek().type == tokenType)
                return true;
        }
        return false;
    }

    private boolean checkNext(TokenType type) {
        if (isAtEnd() || current + 1 >= tokens.size())
            return false;
        return tokens.get(current + 1).type == type;
    }

    private boolean isAtEnd() {
        return peek().type == EOF;
    }

    private Expr primary() {
        if (match(FALSE))
            return new Expr.Literal(false);
        if (match(TRUE))
            return new Expr.Literal(true);
        if (match(THIS))
            return new Expr.This(previous());
        if (match(NIL))
            return new Expr.Literal(null);
        if (match(STRING, NUMBER))
            return new Expr.Literal(previous().literal);
        if (match(LEFT_PAREN)) {
            Expr expression = expression();
            consume(RIGHT_PAREN, "expected ) after expression.");
            return new Expr.Grouping(expression);
        }
        if (match(LEFT_BRACE)) {
            return dictionary();
        }
        if (match(LEFT_SQARE_BRACE)) {
            return array();
        }
        if (match(IDENTIFIER))
            return new Expr.Variable(previous());

        if (match(SUPER)) {
            Token keyword = previous();
            consume(DOT, "Expected . after super");
            Token method = consume(IDENTIFIER, "expected super class method name");
            return new Expr.Super(keyword, method);
        }

        throw error(peek(), "expected expression.");

    }

    private Expr dictionary() {
        List<Expr> keys = new ArrayList<>();
        List<Expr> values = new ArrayList<>();
        if (!check(RIGHT_BRACE)) {
            do {
                keys.add(expression());
                consume(COLON, "Expected :");
                values.add(expression());
            } while (match(COMMA));
        }
        consume(RIGHT_BRACE, "Expected }");
        return new Expr.Dictionary(keys, values);
    }

    private Expr call() {
        Expr expr = primary();
        while (true) {
            if (match(LEFT_PAREN))
                expr = finishCall(expr);
            else if (match(DOT)) {
                Token name = consume(IDENTIFIER, "Expected property name after '.'");
                expr = new Expr.Get(expr, name);
            } else if (match(LEFT_SQARE_BRACE)) {
                Expr indexExpr = expression();
                Token token = consume(RIGHT_SQUARE_BRACE, "Expected ]");
                expr = new Expr.KeyAccess(expr, indexExpr, token);
            } else
                break;
        }
        return expr;
    }

    private Stmt.Function function(String kind) {
        Token name = consume(IDENTIFIER, "Expected function name");
        consume(LEFT_PAREN, "Expected ( after " + kind + " name");
        List<Token> parameters = new ArrayList<>();
        Token spread = null;
        if (!check(RIGHT_PAREN)) {
            do {
                if (spread != null) {
                    throw error(peek(), "Variadic parameter must be the last parameter");
                }
                if (match(SPREAD)) {
                    spread = previous();
                }
                parameters.add(
                        consume(IDENTIFIER, "Expected parameter name"));
            } while (match(COMMA));
        }
        consume(RIGHT_PAREN, "Expected )");
        consume(LEFT_BRACE, "expected { before " + kind + " body");
        List<Stmt> body = block();
        return new Stmt.Function(name, parameters, spread, body);
    }

    private Expr finishCall(Expr expr) {
        List<Expr> arguments = new ArrayList<>();
        if (!check(RIGHT_PAREN)) {
            do {
                if (match(SPREAD)) {
                    Token spread = previous();
                    Expr argument = expression();
                    arguments.add(new Expr.Spread(spread, argument));
                } else {
                    arguments.add(expression());
                }
            } while (match(COMMA));
        }
        Token paren = consume(RIGHT_PAREN, "Expected )");
        return new Expr.Call(expr, paren, arguments);
    }

    private Expr unary() {
        if (match(BANG, MINUS, TILDA)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }
        return preFix();
    }

    private Expr preFix() {
        if (match(MINUS_MINUS, PLUS_PLUS)) {
            Token operator = previous();
            Expr expr = postFix();
            Token token;
            if (operator.type == PLUS_PLUS) {
                token = new Token(PLUS, "+", null, operator.line);
            } else {
                token = new Token(MINUS, "-", null, operator.line);
            }
            Expr right = new Expr.Binary(expr, token, new Expr.Literal(1.0));
            expr = createAssignmentExpression(expr, token, right);
            return new Expr.PreFix(operator, expr);
        }
        return postFix();
    }

    private Expr postFix() {
        Expr expr = call();
        if (match(MINUS_MINUS, PLUS_PLUS)) {
            Token operator = previous();
            Token token;
            if (operator.type == PLUS_PLUS) {
                token = new Token(PLUS, "+", null, operator.line);
            } else {
                token = new Token(MINUS, "-", null, operator.line);
            }
            Expr right = new Expr.Binary(expr, token, new Expr.Literal(1.0));
            expr = createAssignmentExpression(expr, token, right);
            expr = new Expr.PostFix(operator, expr);
        }
        return expr;
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
        while (match(OR, PIPE_PIPE)) {
            Token name = previous();
            Expr right = and();
            expr = new Expr.Logical(expr, name, right);
        }
        return expr;
    }

    private Expr and() {
        Expr expr = bitWiseOr();
        while (match(AND, AMPERSAND_AMPRESAND)) {
            Token name = previous();
            Expr right = bitWiseOr();
            expr = new Expr.Logical(expr, name, right);
        }
        return expr;
    }

    private Expr bitWiseOr() {
        Expr expr = bitWiseXOR();
        while (match(PIPE)) {
            Token name = previous();
            Expr right = bitWiseXOR();
            expr = new Expr.Binary(expr, name, right);
        }
        return expr;
    }

    private Expr bitWiseXOR() {
        Expr expr = bitWiseAnd();
        while (match(CARAT)) {
            Token name = previous();
            Expr right = bitWiseAnd();
            expr = new Expr.Binary(expr, name, right);
        }
        return expr;

    }

    private Expr bitWiseAnd() {
        Expr expr = equality();
        while (match(AMPERSAND)) {
            Token name = previous();
            Expr right = equality();
            expr = new Expr.Binary(expr, name, right);
        }
        return expr;

    }

    private Expr assignment() {
        Expr expr = ternary();
        Expr right;
        Token token;
        if (isAssignmentOperator()) {
            if (isCompoundAssignmentOperatorPresent()) {
                advance();
                Token operator = createOperatorTokenFromCompoundAssignment(previous());
                token = previous();
                right = assignment();
                if (token.type == PIPE_PIPE_EQUAL || token.type == AMPERSAND_AMPRESAND_EQUAL) {
                    right = new Expr.Logical(expr, operator, right);
                } else {
                    right = new Expr.Binary(expr, operator, right);
                }
            } else {
                advance();
                token = previous();
                right = assignment();
            }
            return createAssignmentExpression(expr, token, right);
        }
        return expr;
    }

    private Expr createAssignmentExpression(Expr expr, Token token, Expr right) {
        if (expr instanceof Expr.Variable) {
            expr = new Expr.Assign(((Expr.Variable) expr).name, right);
        } else if (expr instanceof Expr.Get) {
            expr = new Expr.Set(((Expr.Get) expr).object, ((Expr.Get) expr).name, right);
        } else if (expr instanceof Expr.KeyAccess) {
            expr = new Expr.KeySet(((Expr.KeyAccess) expr).object, ((Expr.KeyAccess) expr).key, right,
                    token);
        } else {
            throw error(token, "Invalid assignment target");
        }
        return expr;
    }

    private Expr ternary() {
        Expr condition = or();
        if (match(CONDITIONAL)) {
            Expr left = expression();
            consume(COLON, "Expected :");
            Expr right = expression();
            return new Expr.Ternary(condition, left, right);
        }
        return condition;
    }

    private Expr array() {
        List<Expr> elements = new ArrayList<>();
        if (!check(RIGHT_SQUARE_BRACE)) {
            do {
                elements.add(arrayElement());
            } while (match(COMMA));
        }
        consume(RIGHT_SQUARE_BRACE, "Expected ]");
        return new Expr.Array(elements);
    }

    private Expr arrayElement() {
        // elements can be spread or expression
        if (match(SPREAD)) {
            Token spread = previous();
            Expr expr = expression();
            return new Expr.Spread(spread, expr);
        } else {
            return expression();
        }

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
            if (checkVariableDeclaration())
                return;
            switch (peek().type) {
                case CLASS:
                case FUN:
                case FOR:
                case IF:
                case WHILE:
                case RETURN:
                    return;

                default:
                    break;
            }
            advance();
        }
    }

    private Token createOperatorTokenFromCompoundAssignment(Token token) {
        TokenType type;
        String lexeme;
        if (token.type == PLUS_EQUAL) {
            type = PLUS;
            lexeme = "+";
        } else if (token.type == STAR_EQUAL) {
            type = STAR;
            lexeme = "*";
        } else if (token.type == MINUS_EQUAL) {
            type = MINUS;
            lexeme = "-";
        } else if (token.type == SLASH_EQUAL) {
            type = SLASH;
            lexeme = "/";
        } else if (token.type == PIPE_PIPE_EQUAL) {
            type = PIPE_PIPE;
            lexeme = "||";
        } else if (token.type == AMPERSAND_AMPRESAND_EQUAL) {
            type = AMPERSAND_AMPRESAND;
            lexeme = "&&";
        } else if (token.type == AMPERSAND_EQUAL) {
            type = AMPERSAND;
            lexeme = "&";
        } else if (token.type == PIPE_EQUAL) {
            type = PIPE;
            lexeme = "|";
        } else {
            throw error(token,
                    "Can't create arithmetic operator token as the token is not compound assignment operator");
        }
        return new Token(type, lexeme, null, token.line);

    }

    /*
     * Check if the statement is a variable declaration statement. It can be either
     * long declaration var a = 10; or short declaration a := 10;
     */
    private boolean checkVariableDeclaration() {
        return check(VAR) || (check(IDENTIFIER) && checkNext(COLON_EQUAL));
    }

    /*
     * Check if the current token is a compound assignment operator += , -=, *=, /=
     * , ||=, &&=, |=, &=, ^=
     */
    private boolean isCompoundAssignmentOperatorPresent() {
        return (check(PLUS_EQUAL, MINUS_EQUAL, STAR_EQUAL, SLASH_EQUAL, PIPE_PIPE_EQUAL, AMPERSAND_AMPRESAND_EQUAL,
                PIPE_EQUAL, AMPERSAND_EQUAL));
    }

    /*
     * Check if the current token is an assignment operator = or compound assignment
     * operator
     */
    private boolean isAssignmentOperator() {
        return isCompoundAssignmentOperatorPresent() || check(EQUAL);
    }
}
