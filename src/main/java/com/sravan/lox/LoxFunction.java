package com.sravan.lox;

import java.util.List;

public interface LoxFunction extends LoxCallable {
    int arity();

    Object call(Interpreter interpreter, List<Object> arguments);

    LoxFunction bind(LoxInstance instance);
}
