package com.sravan.lox;

import static com.sravan.lox.TokenType.EOF;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Predicate;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

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
        try {
            System.out.println("Lox REPL");
            System.out.println("Press Ctrl+C to exit");
            Terminal terminal = TerminalBuilder.builder().system(true).build();
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();

            while (true) {
                String line = multiLine(reader);
                if (line == null)
                    break;
                run(line);
                hadError = false;
                hadRuntimeError = false;

            }
        } catch (UserInterruptException e) {
            System.out.println("Exiting...");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Exiting...");
        }
    }

    private static String multiLine(LineReader reader) {
        String line = reader.readLine("> ");
        if (line == null)
            return null;
        int openBraces = 0;
        for (char c : line.toCharArray()) {
            if (c == '{' || c == '(' || c == '[') {
                openBraces++;
            } else if (c == '}' || c == ')' || c == ']') {
                openBraces--;
            }
        }
        while (openBraces > 0) {
            String nextLine = reader.readLine("... ");
            if (nextLine == null)
                break;
            line += "\n" + nextLine;
            for (char c : nextLine.toCharArray()) {
                if (c == '{' || c == '(' || c == '[') {
                    openBraces++;
                } else if (c == '}' || c == ')' || c == ']') {
                    openBraces--;
                }
            }
        }
        return line;
    }

    public static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        filterCommentTokens(tokens);
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
        if (error.token == null)
            System.err.println("Runtime error: " + error.getMessage());
        else
            System.err.println("Runtime error: " + error.getMessage() + "\n[line " + error.token.line + "]");
        hadRuntimeError = true;
    }

    public static void report(int line, String where, String message) {
        System.err.println(
                "[line " + line + "] Error " + where + ": " + message);
        hadError = true;
    }

    private static void filterCommentTokens(List<Token> tokens) {
        tokens.removeIf(new Predicate<Token>() {
            @Override
            public boolean test(Token t) {
                return t.type == TokenType.COMMENT;
            }
        });
    }

    public static String stringify(Object value) {
        if (value == null)
            return "nil";
        String text = value.toString();
        if (value instanceof Double) {
            if (text.endsWith(".0")) {
                return text.substring(0, text.length() - 2);
            }
            return text;
        }
        return text;
    }

}
