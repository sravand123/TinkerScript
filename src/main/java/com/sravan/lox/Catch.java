package com.sravan.lox;

public class Catch extends RuntimeException {
    Object value;
    Token token;

    Catch(Token token, Object value) {
        super(null, null, false, false);
        this.value = value;
        this.token = token;
    }
}
