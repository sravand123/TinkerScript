package tinkerscript;

import static tinkerscript.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String source;
    private final ArrayList<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    public Boolean hadError = false;
    private static final Map<String, TokenType> keywords = new HashMap<>();
    static {
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
        keywords.put("try", TRY);
        keywords.put("catch", CATCH);
        keywords.put("throw", THROW);
        keywords.put("break", BREAK);
        keywords.put("continue", CONTINUE);
        keywords.put("static", STATIC);
        keywords.put("switch", SWITCH);
        keywords.put("case", CASE);
        keywords.put("default", DEFAULT);
    }

    Scanner(String source) {
        this.source = source;
    }

    private void error(int line, String message) {
        Compiler.error(line, message);
        hadError = true;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                if (checkSequence("..")) {
                    advance();
                    advance();
                    addToken(SPREAD);
                } else {
                    addToken(DOT);
                }
                break;
            case '-':
                if (match('>'))
                    addToken(ARROW);
                else
                    addToken(match('-') ? MINUS_MINUS : match('=') ? MINUS_EQUAL : MINUS);
                break;
            case '+':
                addToken(match('+') ? PLUS_PLUS : match('=') ? PLUS_EQUAL : PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(match('=') ? STAR_EQUAL : STAR);
                break;
            case '?':
                addToken(CONDITIONAL);
                break;
            case ':':
                addToken(match('=') ? COLON_EQUAL : COLON);
                break;
            case '[':
                addToken(LEFT_SQARE_BRACE);
                break;
            case ']':
                addToken(RIGHT_SQUARE_BRACE);
                break;
            case '|':
                if (match('|')) {
                    if (match('='))
                        addToken(PIPE_PIPE_EQUAL);
                    else
                        addToken(PIPE_PIPE);
                } else if (match('=')) {
                    addToken(PIPE_EQUAL);
                } else {
                    addToken(PIPE);
                }
                break;
            case '&':
                if (match('&')) {
                    if (match('='))
                        addToken(AMPERSAND_AMPRESAND_EQUAL);
                    else
                        addToken(AMPERSAND_AMPRESAND);
                } else if (match('=')) {
                    addToken(AMPERSAND_EQUAL);
                } else {
                    addToken(AMPERSAND);
                }
                break;
            case '~':
                addToken(TILDA);
                break;
            case '^':
                addToken(match('=') ? CARAT_EQUAL : CARAT);
                break;
            case '%':
                addToken(match('=') ? PERCENTAGE_EQUAL : PERCENTAGE);
                break;
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '/':
                if (match('/')) {
                    while (!isAtEnd() && peek() != '\n') {
                        advance();
                    }
                } else if (match('*')) {
                    int nestedLevel = 1;
                    while (!isAtEnd()) {
                        if (peek() == '*' && peekNext() == '/') {
                            advance();
                            advance();
                            nestedLevel--;
                        } else if (peek() == '/' && peekNext() == '*') {
                            advance();
                            advance();
                            nestedLevel++;
                        } else {
                            if (peek() == '\n')
                                line++;
                            advance();
                        }
                        if (nestedLevel == 0)
                            break;
                    }
                    if (isAtEnd() && nestedLevel > 0) {
                        error(line, "Unterminated comment.");
                    }
                } else
                    addToken(match('=') ? SLASH_EQUAL : SLASH);
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            case '"':
                string();
                break;

            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c))
                    identifier();
                else {
                    error(line, "Unexpected Error.");
                }
                break;
        }
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }

    private boolean checkSequence(String expected) {
        if (isAtEnd())
            return false;
        if (current + expected.length() > source.length())
            return false;

        return source.substring(start, start + expected.length()).equals(expected);
    }

    private boolean match(char c) {
        if (isAtEnd())
            return false;
        if (source.charAt(current) != c)
            return false;
        current++;
        return true;
    }

    private char peek() {
        if (isAtEnd())
            return '\0';
        return source.charAt(current);
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n')
                line++;
            if (peek() == '\\' && peekNext() == '\\') {
                advance();
            } else if (peek() == '\\' && peekNext() == '"') {
                advance();
            }
            advance();
        }
        if (peek() != '"') {
            error(line, "Unterminated string.");
            return;
        }

        advance();
        String value = source.substring(start + 1, current - 1);
        value = escapeString(value);
        addToken(STRING, value);
    }

    private String escapeString(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\\') {
                i++;
                if (i < s.length()) {
                    switch (s.charAt(i)) {
                        case 'n':
                            sb.append('\n');
                            break;
                        case 't':
                            sb.append('\t');
                            break;
                        case 'r':
                            sb.append('\r');
                            break;
                        case 'b':
                            sb.append('\b');
                            break;
                        case 'f':
                            sb.append('\f');
                            break;
                        case '\\':
                            sb.append('\\');
                            break;
                        case '"':
                            sb.append('"');
                            break;
                        case 'u':
                            if (i + 4 < s.length()) {
                                String hex = s.substring(i + 1, i + 5);
                                try {
                                    int code = Integer.parseInt(hex, 16);
                                    sb.append((char) code);
                                    i += 4;
                                } catch (NumberFormatException e) {
                                    error(line, "Invalid unicode sequence.");
                                }
                            } else {
                                error(line, "Invalid unicode sequence.");
                            }
                            break;
                        default:
                            error(line, "Invalid escape character.");
                            break;
                    }
                } else {
                    error(line, "Invalid escape character.");
                }
            } else {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }

    private char peekNext() {
        if (current + 1 >= source.length())
            return '\0';
        return source.charAt(current + 1);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }
        if (peek() == '.' && isDigit(peekNext())) {
            advance();
            while (isDigit(peek())) {
                advance();
            }
        }
        addToken(NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c == '_');
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }
        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) {
            type = IDENTIFIER;
        }
        addToken(type);

    }
}
