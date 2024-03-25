package com.sravan.lox;

public class BreakOut extends RuntimeException {
    Token token;

    BreakOut(Token token) {
        this.token = token;
    }
}
