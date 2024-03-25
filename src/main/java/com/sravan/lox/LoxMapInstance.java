package com.sravan.lox;

import java.util.HashMap;
import java.util.Map;

public class LoxMapInstance extends LoxInstance {
    private final Map<Object, Object> fields = new HashMap<>();

    LoxMapInstance(LoxClass klass, Map<Object, Object> fields) {
        super(klass);
        this.fields.putAll(fields);
    }

    Object get(Token token, Object key) {
        if (fields.containsKey(key)) {
            return fields.get(key);
        }
        throw new RuntimeError(token, "Undefined key '" + key + "'.");
    }

    void set(Token token, Object key, Object value) {
        fields.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (Map.Entry<Object, Object> entry : fields.entrySet()) {
            builder.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        builder.append("}");
        return builder.toString();
    }
}
