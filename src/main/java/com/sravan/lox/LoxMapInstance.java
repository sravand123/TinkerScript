package com.sravan.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public List<Object> getKeys() {
        return new ArrayList<>(fields.keySet());
    }

    public List<Object> getValues() {
        return new ArrayList<>(fields.values());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (Map.Entry<Object, Object> entry : fields.entrySet()) {
            builder.append(Lox.stringify(entry.getKey())).append(": ").append(Lox.stringify(entry.getValue()))
                    .append(", ");
        }
        if (!fields.isEmpty()) {
            builder.delete(builder.length() - 2, builder.length());
        }
        builder.append("}");
        return builder.toString();
    }
}
