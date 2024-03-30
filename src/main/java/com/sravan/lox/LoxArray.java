package com.sravan.lox;

import java.util.List;

public class LoxArray extends LoxInstance {
    final List<Object> elements;

    LoxArray(LoxClass klass, List<Object> elements) {
        super(klass);
        this.elements = elements;
    }

    Object get(Token token, int index) {
        if (index >= this.elements.size()) {
            throw new RuntimeError(token, "Index " + index + " out of range.");
        }
        return this.elements.get(index);
    }

    void set(Token token, int index, Object value) {
        if (index >= this.elements.size()) {
            throw new RuntimeError(token, "Index " + index + " out of range.");
        }
        this.elements.set(index, value);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < elements.size(); i++) {
            builder.append(Lox.stringify(elements.get(i)));
            if (i != elements.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
