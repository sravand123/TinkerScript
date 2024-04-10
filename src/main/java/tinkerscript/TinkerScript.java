package tinkerscript;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

public class TinkerScript {
    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("Usage: tinkerscript [script]");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    public static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        Compiler compiler = new Compiler(CompilerMode.FILE);
        compiler.run(new String(bytes, Charset.defaultCharset()));
        if (compiler.hadError)
            System.exit(65);
        if (compiler.hadRuntimeError)
            System.exit(70);
    }

    public static void runPrompt() throws IOException {
        try {
            System.out.println("Welcome To TinkerScript REPL");
            System.out.println("Press Ctrl+C or Ctrl+D to exit");
            Terminal terminal = TerminalBuilder.builder().system(true).build();
            LineReader reader = LineReaderBuilder.builder().terminal(terminal).build();
            // don't escape characters
            reader.setOpt(LineReader.Option.DISABLE_EVENT_EXPANSION);
            Compiler compiler = new Compiler(CompilerMode.REPL);
            while (true) {
                String line = multiLine(reader);
                if (line == null)
                    break;
                compiler.run(line);
                compiler.clearError();
            }
        } catch (UserInterruptException e) {
            System.out.println("Exiting...");
        } catch (EndOfFileException e) {
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
