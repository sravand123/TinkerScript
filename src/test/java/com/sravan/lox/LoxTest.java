package com.sravan.lox;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;

public class LoxTest {
    private static final String testDir = "src/test/java/com/sravan/lox";

    private static final void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        Lox.run(new String(bytes, Charset.defaultCharset()));
    }

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    @Test
    public void forLoop() throws IOException {

        runFile(testDir + "/testcases/ForLoop.lox");
        assertEquals("0 1 2 3 4 5 6 7 8 9 ", systemOutRule.getLog());
    }

    @Test
    // While loop
    public void whileLoop() throws IOException {
        runFile(testDir + "/testcases/WhileLoop.lox");
        assertEquals("0 1 2 3 4 5 6 7 8 9 ", systemOutRule.getLog());
    }

    // declaration
    @Test
    public void declaration() throws IOException {
        runFile(testDir + "/testcases/Declaration.lox");
        assertEquals("16", systemOutRule.getLog());
    }

    // spread operator
    @Test
    public void spreadOperator() throws IOException {
        runFile(testDir + "/testcases/ArraySpread.Lox");
        assertEquals("123456", systemOutRule.getLog());

    }

    // class
    @Test
    public void classTest() throws IOException {
        runFile(testDir + "/testcases/Class.Lox");
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
        runFile(testDir + "/testcases/Map.lox");
        String result = systemOutRule.getLog();
        assertEquals("1 2 true 2.3", result);
    }

    @Test
    public void testVariadicFunction() throws IOException {
        runFile(testDir + "/testcases/VariadicFunc.lox");
        String result = systemOutRule.getLog();
        assertEquals("Test passed!", result);
    }

    // trycatch
    @Test
    public void testTryCatch() throws IOException {
        runFile(testDir + "/testcases/TryCatch.lox");
        String result = systemOutRule.getLog();
        assertEquals("Test passed!", result);
    }

    // break
    @Test
    public void testBreak() throws IOException {
        runFile(testDir + "/testcases/Break.lox");
        String result = systemOutRule.getLog();
        assertEquals("Test passed!", result);
    }

    // continue
    @Test
    public void testContinue() throws IOException {
        runFile(testDir + "/testcases/Continue.lox");
        String result = systemOutRule.getLog();
        assertEquals("Test passed!", result);
    }

}
