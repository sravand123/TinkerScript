package tinkerscript;

import java.util.ArrayList;
import java.util.List;

public class LangArray extends LangInstance {
    List<Object> elements;
    final LangClass klass;

    LangArray(LangClass klass, List<Object> elements) {
        super(klass);
        this.klass = klass;
        this.elements = new ArrayList<>(elements);
    }

    Object get(Token token, int index) {
        if (index >= this.elements.size()) {
            throw new RuntimeError(token, "Index " + index + " out of range.");
        }
        return this.elements.get(index);
    }

    Object getSlice(Token token, int start, int end) {
        if (start > end) {
            start = end;
        }
        if (end > this.elements.size()) {
            end = this.elements.size();
        }
        if (start >= this.elements.size()) {
            throw new RuntimeError(token, "Index " + start + " out of range.");
        }
        List<Object> slice = this.elements.subList(start, end);
        return new LangArray(this.klass, slice);
    }

    void set(Token token, int index, Object value) {
        if (index >= this.elements.size()) {
            throw new RuntimeError(token, "Index " + index + " out of range.");
        }
        this.elements.set(index, value);
    }

    void push(Object value) {
        this.elements.add(value);
    }

    Object pop() {
        if (this.elements.size() == 0) {
            throw new RuntimeError(null, "Can't pop from an empty array.");
        }
        return this.elements.remove(this.elements.size() - 1);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < elements.size(); i++) {
            builder.append(TinkerScript.stringify(elements.get(i)));
            if (i != elements.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
