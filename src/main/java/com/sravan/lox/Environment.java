package com.sravan.lox;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> values = new HashMap<>();

    void define(String name, Object value) {
        values.put(name, value);
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    Object assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            return values.put(name.lexeme, value);
        }
        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'");
    }

}
