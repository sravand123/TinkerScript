package com.sravan.lox;

import static com.sravan.lox.TokenType.EOF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
    static boolean hadError = false;
    static boolean hadRuntimeError = false;
    private static final Interpreter interpreter = new Interpreter();

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    public static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
        if (hadError)
            System.exit(65);
        if (hadRuntimeError)
            System.exit(70);
    }

    public static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        while (true) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line == null)
                break;
            run(line);
            hadError = false;
            hadRuntimeError = false;

        }
    }

    public static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> statements = parser.parse();
        if (hadError)
            return;
        Resolver resolver = new Resolver(interpreter);
        resolver.resolve(statements);
        if (hadError)
            return;
        interpreter.interpret(statements);
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
        System.err.println("Runtime error: " + error.getMessage() + "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }

    public static void report(int line, String where, String message) {
        System.err.println(
                "[line " + line + "] Error " + where + ": " + message);
        hadError = true;
    }

}
