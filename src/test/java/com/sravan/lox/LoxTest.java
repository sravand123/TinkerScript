package com.sravan.lox;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;

public class LoxTest {

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    @Test
    public void forLoop() throws IOException {

        Lox.runFile("src/test/java/com/sravan/lox/ForLoop.lox");
        assertEquals("0 1 2 3 4 5 6 7 8 9 ", systemOutRule.getLog());
    }

    @Test
    // While loop
    public void whileLoop() throws IOException {
        Lox.runFile("src/test/java/com/sravan/lox/WhileLoop.lox");
        assertEquals("0 1 2 3 4 5 6 7 8 9 ", systemOutRule.getLog());
    }

    // declaration
    @Test
    public void declaration() throws IOException {
        Lox.runFile("src/test/java/com/sravan/lox/Declaration.lox");
        assertEquals("16", systemOutRule.getLog());
    }

    // spread operator
    @Test
    public void spreadOperator() throws IOException {
        Lox.runFile("src/test/java/com/sravan/lox/ArraySpread.Lox");
        assertEquals("123456", systemOutRule.getLog());

    }

    // class
    @Test
    public void classTest() throws IOException {
        Lox.runFile("src/test/java/com/sravan/lox/Class.Lox");
        assertEquals("Hello, my name is Alice and I am 25 years old.", systemOutRule.getLog());
    }

    @Test
    public void testComplexString() {
        String input = "print("
                + "\"Hello,\\n\\t\\\"\\u0048\\u0065\\u006C\\u006C\\u006F\\u002C\\u0020\\u0057\\u006F\\u0072\\u006C\\u0064!\""
                + ");";
        String expectedOutput = "Hello,\n\t\"Hello, World!";
        Lox.run(input);
        String result = systemOutRule.getLog();
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testMap() throws IOException {
        Lox.runFile("src/test/java/com/sravan/lox/Map.lox");
        String result = systemOutRule.getLog();
        assertEquals("1 2 true 2.3", result);
    }

    @Test
    public void testVariadicFunction() throws IOException {
        Lox.runFile("src/test/java/com/sravan/lox/VariadicFunc.lox");
        String result = systemOutRule.getLog();
        assertEquals("Test passed!", result);
    }

    // trycatch
    @Test
    public void testTryCatch() throws IOException {
        Lox.runFile("src/test/java/com/sravan/lox/TryCatch.lox");
        String result = systemOutRule.getLog();
        assertEquals("Test passed!", result);
    }

    // break
    @Test
    public void testBreak() throws IOException {
        Lox.runFile("src/test/java/com/sravan/lox/Break.lox");
        String result = systemOutRule.getLog();
        assertEquals("Test passed!", result);
    }

    // continue
    @Test
    public void testContinue() throws IOException {
        Lox.runFile("src/test/java/com/sravan/lox/Continue.lox");
        String result = systemOutRule.getLog();
        assertEquals("Test passed!", result);
    }

}
