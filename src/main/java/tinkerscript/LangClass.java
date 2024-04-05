package tinkerscript;

import java.util.List;
import java.util.Map;

public class LangClass implements LangCallable {
    final String name;
    private final Map<String, LangFunction> methods;
    private final Map<String, LangFunction> staticMethods;
    final LangClass superClass;

    LangClass(String name, Map<String, LangFunction> methods,
            Map<String, LangFunction> staticMethods,
            LangClass superClass) {
        this.name = name;
        this.methods = methods;
        this.superClass = superClass;
        this.staticMethods = staticMethods;
    }

    LangFunction findMethod(String name) {
        if (methods.containsKey(name))
            return methods.get(name);
        if (superClass != null) {
            return superClass.findMethod(name);
        }

        return null;
    }

    LangFunction getStaticMethod(Token name) {
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
        LangFunction inititalizer = findMethod("init");
        if (inititalizer != null)
            return inititalizer.arity();
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        LangInstance instance = new LangInstance(this);
        LangFunction initializer = findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }
}
