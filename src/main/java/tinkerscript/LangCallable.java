package tinkerscript;

import java.util.List;

public interface LangCallable {
    int arity();

    Object call(Interpreter interpreter, List<Object> arguments);
}