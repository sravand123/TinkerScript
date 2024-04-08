package tinkerscript;

import java.util.HashMap;
import java.util.Map;

public class LangInstance {
    private LangClass klass;
    private final Map<String, Object> fields = new HashMap<>();

    LangInstance(LangClass klass) {
        this.klass = klass;
    }

    Object get(Token name) {
        if (fields.containsKey(name.lexeme)) {
            return fields.get(name.lexeme);
        }
        LangFunction method = klass.findMethod(name.lexeme);
        if (method != null)
            return method.bind(this);
        throw new RuntimeError(name, "Undefined property '" + name.lexeme + "'.");
    }

    void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }

    void set(String name, Object value) {
        fields.put(name, value);
    }

    Map<String, Object> getFields() {
        return fields;
    }

    @Override
    public String toString() {
        return klass.name + " instance";
    }
}
