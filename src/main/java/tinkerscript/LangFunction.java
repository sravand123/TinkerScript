package tinkerscript;

import java.util.List;

public interface LangFunction extends LangCallable {
    int arity();

    Object call(Interpreter interpreter, List<Object> arguments);

    LangFunction bind(LangInstance instance);
}
