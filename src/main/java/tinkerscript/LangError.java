package tinkerscript;

import java.util.Map;

public class LangError extends LangInstance {
    LangError(LangClass klass) {
        super(klass);
    }

    @Override
    public String toString() {
        Map<String, Object> fields = getFields();
        if (fields.containsKey("message")) {
            return "Error: " + fields.get("message").toString();
        } else {
            return "Error";
        }
    }
}
