package com.sravan.lox;

import java.util.List;

public class UserFunction implements LoxFunction {
    final Stmt.Function declaration;
    final Environment closure;
    final boolean isInitializer;

    UserFunction(Stmt.Function declaration, Environment closure, boolean isInitializer) {
        this.declaration = declaration;
        this.closure = closure;
        this.isInitializer = isInitializer;
    }

    @Override
    public int arity() {
        if (declaration.spread != null)
            return -1;
        return declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);
        List<Token> params = declaration.params;
        for (int i = 0; i < params.size() - 1; i++) {
            environment.define(params.get(i).lexeme, arguments.get(i));
        }
        if (arguments.size() > 0) {
            Object lastArgument = arguments.get(arguments.size() - 1);
            if (declaration.spread != null) {
                List<Object> subList = arguments.subList(params.size() - 1, arguments.size());
                LoxClass arrayClass = (LoxClass) interpreter.globals.get("Array");
                lastArgument = new LoxArray(arrayClass, subList);
            }
            environment.define(params.get(params.size() - 1).lexeme, lastArgument);
        } else {
            if (declaration.spread != null) {
                LoxClass arrayClass = (LoxClass) interpreter.globals.get("Array");
                environment.define(params.get(params.size() - 1).lexeme,
                        new LoxArray(arrayClass, List.of()));
            }
        }
        try {
            interpreter.executeBlock(declaration.body, environment);
        } catch (Return returnValue) {
            if (isInitializer)
                return closure.getAt("this", 0);
            return returnValue.value;
        }
        if (isInitializer)
            return closure.getAt("this", 0);
        return null;

    }

    public UserFunction bind(LoxInstance instance) {
        Environment environment = new Environment(closure);
        environment.define("this", instance);
        return new UserFunction(declaration, environment, isInitializer);
    }

    @Override
    public String toString() {
        if (declaration.name == null) {
            return "<fn anonymous>";
        }
        return "<fn " + declaration.name.lexeme + ">";
    }

}
