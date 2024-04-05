package tinkerscript;

public class Catch extends RuntimeError {
    Object value;
    Token token;

    Catch(Token token, Object value) {
        super(token, value.toString());
        this.value = value;
        this.token = token;
    }
}
