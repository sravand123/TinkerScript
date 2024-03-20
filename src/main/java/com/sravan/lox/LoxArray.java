package com.sravan.lox;

import java.util.List;

public class LoxArray {
    final List<Object> elements;

    LoxArray(List<Object> elements) {
        this.elements = elements;
    }

    Object get(Token token, int index) {
        if (index >= this.elements.size()) {
            throw new RuntimeError(token, "Index " + index + "out of range");
        }
        return this.elements.get(index);
    }

    void set(Token token, int index, Object value) {
        if (index >= this.elements.size()) {
            throw new RuntimeError(token, "Index " + index + "out of range");
        }
        this.elements.set(index, value);
    }

    @Override
    public String toString() {
        return "<array>";
    }
}
