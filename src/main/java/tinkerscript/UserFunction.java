package tinkerscript;

import java.util.List;

public class UserFunction implements LangFunction {
    final Stmt.Function declaration;
    final Environment closure;
    final boolean isInitializer;
    final boolean isGetter;

    UserFunction(Stmt.Function declaration, Environment closure, boolean isInitializer, boolean isGetter) {
        this.declaration = declaration;
        this.closure = closure;
        this.isInitializer = isInitializer;
        this.isGetter = isGetter;
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
                LangClass arrayClass = (LangClass) interpreter.globals.get("Array");
                lastArgument = new LangArray(arrayClass, subList);
            }
            environment.define(params.get(params.size() - 1).lexeme, lastArgument);
        } else {
            if (declaration.spread != null) {
                LangClass arrayClass = (LangClass) interpreter.globals.get("Array");
                environment.define(params.get(params.size() - 1).lexeme,
                        new LangArray(arrayClass, List.of()));
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

    public UserFunction bind(LangInstance instance) {
        Environment environment = new Environment(closure);
        environment.define("this", instance);
        return new UserFunction(declaration, environment, isInitializer, isGetter);
    }

    @Override
    public String toString() {
        if (declaration.name == null) {
            return "<fn anonymous>";
        }
        return "<fn " + declaration.name.lexeme + ">";
    }

}
