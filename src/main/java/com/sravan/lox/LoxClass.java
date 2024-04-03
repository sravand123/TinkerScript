package com.sravan.lox;

import java.util.List;
import java.util.Map;

public class LoxClass implements LoxCallable {
    final String name;
    private final Map<String, LoxFunction> methods;
    private final Map<String, LoxFunction> staticMethods;
    final LoxClass superClass;

    LoxClass(String name, Map<String, LoxFunction> methods,
            Map<String, LoxFunction> staticMethods,
            LoxClass superClass) {
        this.name = name;
        this.methods = methods;
        this.superClass = superClass;
        this.staticMethods = staticMethods;
    }

    LoxFunction findMethod(String name) {
        if (methods.containsKey(name))
            return methods.get(name);
        if (superClass != null) {
            return superClass.findMethod(name);
        }

        return null;
    }

    LoxFunction getStaticMethod(Token name) {
        if (staticMethods.containsKey(name.lexeme))
            return staticMethods.get(name.lexeme);
        if (superClass != null) {
            return superClass.getStaticMethod(name);
        }
        throw new RuntimeError(name, "Undefined property '" + name.lexeme + "'.");
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int arity() {
        LoxFunction inititalizer = findMethod("init");
        if (inititalizer != null)
            return inititalizer.arity();
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        LoxInstance instance = new LoxInstance(this);
        LoxFunction initializer = findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }
}
