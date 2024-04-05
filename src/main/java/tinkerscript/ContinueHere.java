package tinkerscript;

public class ContinueHere extends RuntimeException {
    Token token;

    ContinueHere(Token token) {
        this.token = token;
    }
}
