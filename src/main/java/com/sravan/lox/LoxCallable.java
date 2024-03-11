package com.sravan.lox;

import java.util.List;

/**
 * LoxCallable
 */
public interface LoxCallable {
    int arity();

    Object call(Interpreter interpreter, List<Object> arguments);
}