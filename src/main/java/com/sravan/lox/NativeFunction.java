package com.sravan.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class NativeFunction implements LoxFunction {
    static class ArrayLength extends NativeFunction {

        @Override
        public int arity() {
            return 1;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            Object instance = arguments.get(0);
            if (!(instance instanceof LoxArray)) {
                throw new RuntimeError(null, "Invalid argument type. Expected array.");
            }
            return (double) ((LoxArray) instance).elements.size();
        }

        @Override
        public LoxFunction bind(LoxInstance instance) {
            return this;
        }
    }

    // clock
    static class Clock extends NativeFunction {
        @Override
        public int arity() {
            return 0;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            return (double) System.currentTimeMillis() / 1000.0;
        }

        @Override
        public LoxFunction bind(LoxInstance instance) {
            return this;
        }
    }

    static class ArrayFunction {
        static class Push extends NativeFunction {

            private LoxArray instance;

            @Override
            public int arity() {
                return 1;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                Object value = arguments.get(0);
                instance.push(value);
                return null;
            }

            @Override
            public LoxFunction bind(LoxInstance instance) {
                if (!(instance instanceof LoxArray)) {
                    throw new RuntimeError(null, "Invalid instance. Expected array.");
                }
                this.instance = (LoxArray) instance;
                return this;
            }
        }

        static class Pop extends NativeFunction {

            private LoxArray instance;

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return instance.pop();
            }

            @Override
            public LoxFunction bind(LoxInstance instance) {
                if (!(instance instanceof LoxArray)) {
                    throw new RuntimeError(null, "Invalid instance. Expected array.");
                }
                this.instance = (LoxArray) instance;
                return this;
            }
        }
    }

    static class MapFunction {
        static class Keys extends NativeFunction {
            private LoxMapInstance instance;

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                LoxClass klass = (LoxClass) interpreter.globals.get("Array");
                LoxArray array = new LoxArray(klass, instance.getKeys());
                return array;
            }

            @Override
            public LoxFunction bind(LoxInstance instance) {
                if (!(instance instanceof LoxMapInstance)) {
                    throw new RuntimeError(null, "Invalid instance. Expected map.");
                }
                this.instance = (LoxMapInstance) instance;
                return this;
            }
        }

        static class Values extends NativeFunction {

            private LoxMapInstance instance;

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                LoxClass klass = (LoxClass) interpreter.globals.get("Array");
                LoxArray array = new LoxArray(klass, instance.getValues());
                return array;
            }

            @Override
            public LoxFunction bind(LoxInstance instance) {
                if (!(instance instanceof LoxMapInstance)) {
                    throw new RuntimeError(null, "Invalid instance. Expected map.");
                }
                this.instance = (LoxMapInstance) instance;
                return this;
            }
        }
    }
    static class StringLength extends NativeFunction {
        @Override
        public int arity() {
            return 1;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            if (!(arguments.get(0) instanceof String)) {
                throw new RuntimeError(null, "Invalid argument type. Expected string.");
            }
            return (double) ((String) arguments.get(0)).length();
        }

        @Override
        public LoxFunction bind(LoxInstance instance) {
            return this;
        }
    }

    // Input
    static class Input extends NativeFunction {
        @Override
        public int arity() {
            return -1;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            try {
                if (arguments.size() > 0 && (arguments.get(0) instanceof String)) {
                    System.out.print(arguments.get(0));
                }
                String input = new BufferedReader(new InputStreamReader(System.in)).readLine();
                return input;
            } catch (Exception e) {
                throw new RuntimeError(null, "Error reading input.");
            }
        }

        @Override
        public LoxFunction bind(LoxInstance instance) {
            return this;
        }
    }

    // toInt
    static class ToNumber extends NativeFunction {
        @Override
        public int arity() {
            return 1;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            Object instance = arguments.get(0);
            if (!(instance instanceof String)) {
                throw new RuntimeError(null, "Invalid argument type. Expected string.");
            }
            try {
                return Double.parseDouble((String) instance);
            } catch (NumberFormatException e) {
                throw new RuntimeError(null, "Cannot convert to number.");
            }
        }

        @Override
        public LoxFunction bind(LoxInstance instance) {
            return this;
        }
    }

    static class ToString extends NativeFunction {
        @Override
        public int arity() {
            return 1;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            return Lox.stringify(arguments.get(0));
        }

        @Override
        public LoxFunction bind(LoxInstance instance) {
            return this;
        }
    }

    @Override
    public String toString() {
        return "<native fn>";
    }

    // print
    static class Print extends NativeFunction {
        @Override
        public int arity() {
            return -1;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            List<String> strings = new ArrayList<>();
            for (Object arg : arguments) {
                strings.add(Lox.stringify(arg));
            }
            String formattedString = String.join(" ", strings);
            System.out.print(formattedString);
            return null;
        }

        @Override
        public LoxFunction bind(LoxInstance instance) {
            return this;
        }
    }

    // println
    static class Println extends NativeFunction {
        @Override
        public int arity() {
            return -1;
        }

        @Override
        public Object call(Interpreter interpreter, List<Object> arguments) {
            List<String> strings = new ArrayList<>();
            for (Object arg : arguments) {
                strings.add(Lox.stringify(arg));
            }
            String formattedString = String.join(" ", strings);
            System.out.println(formattedString);
            return null;
        }

        @Override
        public LoxFunction bind(LoxInstance instance) {
            return this;
        }
    }

}