package tinkerscript;

import static tinkerscript.TokenType.EOF;

import java.util.List;

public class Compiler {
    public Boolean hadError = false;
    public Boolean hadRuntimeError = false;
    Interpreter interpreter;
    private final CompilerMode mode;

    Compiler(CompilerMode mode) {
        this.mode = mode;
        interpreter = new Interpreter(mode);
    }

    public void run(String source) {
        Scanner scanner = new Scanner(source);
        hadError = scanner.hadError;
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens, mode);
        List<Stmt> statements = parser.parse();
        hadError = parser.hadError;
        if (hadError)
            return;
        Resolver resolver = new Resolver(interpreter);
        resolver.resolve(statements);
        hadError = resolver.hadError;
        if (hadError)
            return;
        interpreter.interpret(statements);
        hadRuntimeError = interpreter.hadRuntimeError;
    }

    public void clearError() {
        hadError = false;
        hadRuntimeError = false;
    }

    static void error(int line, String message) {
        report(line, "", message);
    }

    static void error(Token token, String message) {
        if (token.type == EOF) {
            report(token.line, " at end", message);
            return;
        }
        report(token.line, " at '" + token.lexeme + "'", message);
    }

    static void runtimeError(RuntimeError error) {
        if (error.token == null)
            System.err.println("runtime error: " + error.getMessage());
        else
            System.err.println("runtime error: " + error.getMessage() + " [line " + error.token.line + "]");
    }

    public static void report(int line, String where, String message) {
        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
    }
}
