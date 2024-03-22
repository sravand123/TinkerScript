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
        assertEquals("4 16", systemOutRule.getLog());
    }

    // spread operator
    @Test
    public void spreadOperator() throws IOException {
        Lox.runFile("src/test/java/com/sravan/lox/ArraySpread.Lox");
        assertEquals("123456", systemOutRule.getLog());

    }

    // object spread operator
    // @Test
    // public void objectSpreadOperator() throws IOException {
    // Lox.runFile("src/test/java/com/sravan/lox/ObjectSpread.Lox");
    // assertEquals("1234", systemOutRule.getLog());
    // }

}
