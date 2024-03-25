package com.sravan.lox;

enum TokenType {
    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR,
    CONDITIONAL, COLON, LEFT_SQARE_BRACE, RIGHT_SQUARE_BRACE,
    PIPE, CARAT, AMPERSAND, TILDA, PERCENTAGE,

    // One or two character tokens.
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,
    PLUS_PLUS, MINUS_MINUS,
    PLUS_EQUAL, STAR_EQUAL,
    SLASH_EQUAL, MINUS_EQUAL,
    PIPE_PIPE, AMPERSAND_AMPRESAND,
    PIPE_PIPE_EQUAL, AMPERSAND_AMPRESAND_EQUAL,
    PIPE_EQUAL, AMPERSAND_EQUAL,
    CARAT_EQUAL, COLON_EQUAL,
    PERCENTAGE_EQUAL,
    SPREAD,

    // Literals.
    IDENTIFIER, STRING, NUMBER,

    // Keywords.
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
    RETURN, SUPER, THIS, TRUE, VAR, WHILE,
    TRY, CATCH, THROW,

    COMMENT,
    EOF
}