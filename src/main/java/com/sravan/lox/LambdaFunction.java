package com.sravan.lox;

import java.util.List;

public class LambdaFunction implements LoxFunction {
    final Expr.Lambda lambda;
    final Environment closure;

    LambdaFunction(Expr.Lambda lambda, Environment closure) {
        this.lambda = lambda;
        this.closure = closure;
    }

    @Override
    public int arity() {
        if (lambda.spread != null)
            return -1;
        return lambda.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment environment = new Environment(closure);
        List<Token> params = lambda.params;
        for (int i = 0; i < params.size() - 1; i++) {
            environment.define(params.get(i).lexeme, arguments.get(i));
        }
        if (arguments.size() > 0) {
            Object lastArgument = arguments.get(arguments.size() - 1);
            if (lambda.spread != null) {
                List<Object> subList = arguments.subList(params.size() - 1, arguments.size());
                LoxClass arrayClass = (LoxClass) interpreter.globals.get("Array");
                lastArgument = new LoxArray(arrayClass, subList);
            }
            environment.define(params.get(params.size() - 1).lexeme, lastArgument);
        } else {
            if (lambda.spread != null) {
                LoxClass arrayClass = (LoxClass) interpreter.globals.get("Array");
                environment.define(params.get(params.size() - 1).lexeme,
                        new LoxArray(arrayClass, List.of()));
            }
        }

        return interpreter.evaluate(lambda.body, environment);
    }

    @Override
    public LoxFunction bind(LoxInstance instance) {
        throw new RuntimeError(null, "Lambda cannot be bound to an instance.");
    }

    @Override
    public String toString() {
        return "<lambda>";
    }
}
