package tinkerscript;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemErrRule;
import org.junit.contrib.java.lang.system.SystemOutRule;

public class TestCaseRunner {
    private static final String testDir = "src/test/java/tinkerscript/testcases";


    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    private final String getFinalConsoleOutput(String result, String error) {
        if (error.startsWith("runtime error:")) {
            error = error.replaceAll(" \\[line \\d+\\]", "");
        }
        if (result.isEmpty() && error.isEmpty())
            return "";
        if (error.isEmpty())
            return result;
        if (result.isEmpty())
            return error;
        return result + error;
    }
    private final String testFile(String path) throws IOException {
        Compiler compiler = new Compiler(CompilerMode.FILE);
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        compiler.run(new String(bytes, Charset.defaultCharset()));
        String result = systemOutRule.getLog();
        String error = systemErrRule.getLog();
        String output = getFinalConsoleOutput(result, error);
        return output;
    }

    @Test
    public void lambdas_use() throws IOException {
        String output= testFile(testDir + "/lambdas/use.tis");
        assertEquals("<lambda>\n2\n3\n3\nnil\n2\n0 1 2 \n1 2 3 \n3\n3\n3\n", output);
    }

    @Test
    public void lambdas_scope() throws IOException {
        String output= testFile(testDir + "/lambdas/scope.tis");
        assertEquals("Hello foo\nHello foo\n", output);
    }

    @Test
    public void lambdas_syntax() throws IOException {
        String output= testFile(testDir + "/lambdas/syntax.tis");
        assertEquals("[line 15] Error at ';': Expected '->' after parameters.\n[line 17] Error at ')': Expected parameter name.\n[line 18] Error at ';': Expected expression.\n", output);
    }

    @Test
    public void lambdas_curry() throws IOException {
        String output= testFile(testDir + "/lambdas/curry.tis");
        assertEquals("<lambda>\n3\n6\n1\n", output);
    }

    @Test
    public void ternary_syntax_error() throws IOException {
        String output= testFile(testDir + "/ternary/syntax_error.tis");
        assertEquals("[line 1] Error at '?': Expected expression.\n[line 2] Error at ':': Expected expression.\n[line 3] Error at ';': Expected expression.\n[line 5] Error at 'var': Expected expression.\n[line 6] Error at 'var': Expected expression.\n", output);
    }

    @Test
    public void ternary_evaluation() throws IOException {
        String output= testFile(testDir + "/ternary/evaluation.tis");
        assertEquals("func1\nfunc3\n100 101\n", output);
    }

    @Test
    public void ternary_comparison_condition() throws IOException {
        String output= testFile(testDir + "/ternary/comparison_condition.tis");
        assertEquals("200\n100\n100\n200\n100\n200\n", output);
    }

    @Test
    public void ternary_literal_condition() throws IOException {
        String output= testFile(testDir + "/ternary/literal_condition.tis");
        assertEquals("1\n2\n1\n1\n2\n", output);
    }

    @Test
    public void ternary_precedence() throws IOException {
        String output= testFile(testDir + "/ternary/precedence.tis");
        assertEquals("1\n1\n", output);
    }

    @Test
    public void ternary_statement() throws IOException {
        String output= testFile(testDir + "/ternary/statement.tis");
        assertEquals("", output);
    }

    @Test
    public void closure_close_over_later_variable() throws IOException {
        String output= testFile(testDir + "/closure/close_over_later_variable.tis");
        assertEquals("b\na\n", output);
    }

    @Test
    public void closure_closed_closure_in_function() throws IOException {
        String output= testFile(testDir + "/closure/closed_closure_in_function.tis");
        assertEquals("local\n", output);
    }

    @Test
    public void closure_reuse_closure_slot() throws IOException {
        String output= testFile(testDir + "/closure/reuse_closure_slot.tis");
        assertEquals("a\n", output);
    }

    @Test
    public void closure_assign_to_shadowed_later() throws IOException {
        String output= testFile(testDir + "/closure/assign_to_shadowed_later.tis");
        assertEquals("inner\nassigned\n", output);
    }

    @Test
    public void closure_unused_closure() throws IOException {
        String output= testFile(testDir + "/closure/unused_closure.tis");
        assertEquals("ok\n", output);
    }

    @Test
    public void closure_close_over_function_parameter() throws IOException {
        String output= testFile(testDir + "/closure/close_over_function_parameter.tis");
        assertEquals("param\n", output);
    }

    @Test
    public void closure_unused_later_closure() throws IOException {
        String output= testFile(testDir + "/closure/unused_later_closure.tis");
        assertEquals("a\n", output);
    }

    @Test
    public void closure_shadow_closure_with_local() throws IOException {
        String output= testFile(testDir + "/closure/shadow_closure_with_local.tis");
        assertEquals("closure\nshadow\nclosure\n", output);
    }

    @Test
    public void closure_close_over_method_parameter() throws IOException {
        String output= testFile(testDir + "/closure/close_over_method_parameter.tis");
        assertEquals("param\n", output);
    }

    @Test
    public void closure_open_closure_in_function() throws IOException {
        String output= testFile(testDir + "/closure/open_closure_in_function.tis");
        assertEquals("local\n", output);
    }

    @Test
    public void closure_assign_to_closure() throws IOException {
        String output= testFile(testDir + "/closure/assign_to_closure.tis");
        assertEquals("local\nafter f\nafter f\nafter g\n", output);
    }

    @Test
    public void closure_reference_closure_multiple_times() throws IOException {
        String output= testFile(testDir + "/closure/reference_closure_multiple_times.tis");
        assertEquals("a\na\n", output);
    }

    @Test
    public void closure_nested_closure() throws IOException {
        String output= testFile(testDir + "/closure/nested_closure.tis");
        assertEquals("a\nb\nc\n", output);
    }

    @Test
    public void empty_file() throws IOException {
        String output= testFile(testDir + "/empty_file.tis");
        assertEquals("", output);
    }

    @Test
    public void comments_line_at_eof() throws IOException {
        String output= testFile(testDir + "/comments/line_at_eof.tis");
        assertEquals("ok\n", output);
    }

    @Test
    public void comments_only_line_comment() throws IOException {
        String output= testFile(testDir + "/comments/only_line_comment.tis");
        assertEquals("", output);
    }

    @Test
    public void comments_multiline_with_nesting() throws IOException {
        String output= testFile(testDir + "/comments/multiline_with_nesting.tis");
        assertEquals("", output);
    }

    @Test
    public void comments_multiline_nested_unterminated() throws IOException {
        String output= testFile(testDir + "/comments/multiline_nested_unterminated.tis");
        assertEquals("[line 4] Error: Unterminated comment.\n", output);
    }

    @Test
    public void comments_unicode() throws IOException {
        String output= testFile(testDir + "/comments/unicode.tis");
        assertEquals("ok\n", output);
    }

    @Test
    public void comments_only_line_comment_and_line() throws IOException {
        String output= testFile(testDir + "/comments/only_line_comment_and_line.tis");
        assertEquals("", output);
    }

    @Test
    public void comments_multiline() throws IOException {
        String output= testFile(testDir + "/comments/multiline.tis");
        assertEquals("", output);
    }

    @Test
    public void comments_multiline_single_unterminated() throws IOException {
        String output= testFile(testDir + "/comments/multiline_single_unterminated.tis");
        assertEquals("[line 1] Error: Unterminated comment.\n", output);
    }

    @Test
    public void comments_multiline_unterminated() throws IOException {
        String output= testFile(testDir + "/comments/multiline_unterminated.tis");
        assertEquals("[line 3] Error: Unterminated comment.\n", output);
    }

    @Test
    public void variable_use_global_in_initializer() throws IOException {
        String output= testFile(testDir + "/variable/use_global_in_initializer.tis");
        assertEquals("value\n", output);
    }

    @Test
    public void variable_in_nested_block() throws IOException {
        String output= testFile(testDir + "/variable/in_nested_block.tis");
        assertEquals("outer\n", output);
    }

    @Test
    public void variable_scope_reuse_in_different_blocks() throws IOException {
        String output= testFile(testDir + "/variable/scope_reuse_in_different_blocks.tis");
        assertEquals("first\nsecond\n", output);
    }

    @Test
    public void variable_local_from_method() throws IOException {
        String output= testFile(testDir + "/variable/local_from_method.tis");
        assertEquals("variable\n", output);
    }

    @Test
    public void variable_undefined_global() throws IOException {
        String output= testFile(testDir + "/variable/undefined_global.tis");
        assertEquals("runtime error: Undefined variable 'notDefined'.\n", output);
    }

    @Test
    public void variable_shadow_and_local() throws IOException {
        String output= testFile(testDir + "/variable/shadow_and_local.tis");
        assertEquals("outer\ninner\n", output);
    }

    @Test
    public void variable_early_bound() throws IOException {
        String output= testFile(testDir + "/variable/early_bound.tis");
        assertEquals("outer\nouter\n", output);
    }

    @Test
    public void variable_use_this_as_var() throws IOException {
        String output= testFile(testDir + "/variable/use_this_as_var.tis");
        assertEquals("[line 2] Error at 'this': Expected variable name.\n", output);
    }

    @Test
    public void variable_redeclare_global() throws IOException {
        String output= testFile(testDir + "/variable/redeclare_global.tis");
        assertEquals("nil\n", output);
    }

    @Test
    public void variable_use_nil_as_var() throws IOException {
        String output= testFile(testDir + "/variable/use_nil_as_var.tis");
        assertEquals("[line 2] Error at 'nil': Expected variable name.\n", output);
    }

    @Test
    public void variable_shadow_global() throws IOException {
        String output= testFile(testDir + "/variable/shadow_global.tis");
        assertEquals("shadow\nglobal\n", output);
    }

    @Test
    public void variable_use_false_as_var() throws IOException {
        String output= testFile(testDir + "/variable/use_false_as_var.tis");
        assertEquals("[line 2] Error at 'false': Expected variable name.\n", output);
    }

    @Test
    public void variable_duplicate_local() throws IOException {
        String output= testFile(testDir + "/variable/duplicate_local.tis");
        assertEquals("[line 3] Error at 'a': Already a variable with this name in this scope.\n", output);
    }

    @Test
    public void variable_in_middle_of_block() throws IOException {
        String output= testFile(testDir + "/variable/in_middle_of_block.tis");
        assertEquals("a\na b\na c\na b d\n", output);
    }

    @Test
    public void variable_shadow_local() throws IOException {
        String output= testFile(testDir + "/variable/shadow_local.tis");
        assertEquals("shadow\nlocal\n", output);
    }

    @Test
    public void variable_collide_with_parameter() throws IOException {
        String output= testFile(testDir + "/variable/collide_with_parameter.tis");
        assertEquals("[line 2] Error at 'a': Already a variable with this name in this scope.\n", output);
    }

    @Test
    public void variable_unreached_undefined() throws IOException {
        String output= testFile(testDir + "/variable/unreached_undefined.tis");
        assertEquals("ok\n", output);
    }

    @Test
    public void variable_duplicate_parameter() throws IOException {
        String output= testFile(testDir + "/variable/duplicate_parameter.tis");
        assertEquals("[line 2] Error at 'arg': Already a variable with this name in this scope.\n", output);
    }

    @Test
    public void variable_uninitialized() throws IOException {
        String output= testFile(testDir + "/variable/uninitialized.tis");
        assertEquals("nil\n", output);
    }

    @Test
    public void variable_use_local_in_initializer() throws IOException {
        String output= testFile(testDir + "/variable/use_local_in_initializer.tis");
        assertEquals("[line 3] Error at 'a': Can't read local variable in its own initializer.\n", output);
    }

    @Test
    public void variable_redefine_global() throws IOException {
        String output= testFile(testDir + "/variable/redefine_global.tis");
        assertEquals("2\n", output);
    }

    @Test
    public void variable_undefined_local() throws IOException {
        String output= testFile(testDir + "/variable/undefined_local.tis");
        assertEquals("runtime error: Undefined variable 'notDefined'.\n", output);
    }

    @Test
    public void nil_literal() throws IOException {
        String output= testFile(testDir + "/nil/literal.tis");
        assertEquals("nil\n", output);
    }

    @Test
    public void array_syntax() throws IOException {
        String output= testFile(testDir + "/array/syntax.tis");
        assertEquals("[line 1] Error at ']': Expected expression.\n[line 3] Error at ',': Expected ';' after expression.\n[line 5] Error at ';': Expected ']'.\n[line 7] Error at ',': Expected expression.\n[line 9] Error at 'var': Expected expression.\n", output);
    }

    @Test
    public void array_inside_class() throws IOException {
        String output= testFile(testDir + "/array/inside_class.tis");
        assertEquals("10\n[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]\n9\n[0, 1, 2, 3, 4, 5, 6, 7, 8]\n9\n[0, 1, 2, 3, 4, 5, 6, 7, 8, 10]\n", output);
    }

    @Test
    public void array_access() throws IOException {
        String output= testFile(testDir + "/array/access.tis");
        assertEquals("1\n2\n3\n1 2 3 \n1 2.3 true hello \n1 2 3 4 5 6 7 8 9 \n", output);
    }

    @Test
    public void array_set_out_of_range() throws IOException {
        String output= testFile(testDir + "/array/set_out_of_range.tis");
        assertEquals("runtime error: Index 4 out of range.\n", output);
    }

    @Test
    public void array_invalid_access_string_index() throws IOException {
        String output= testFile(testDir + "/array/invalid_access_string_index.tis");
        assertEquals("runtime error: Invalid index.\n", output);
    }

    @Test
    public void array_invalid_access_decimal_index() throws IOException {
        String output= testFile(testDir + "/array/invalid_access_decimal_index.tis");
        assertEquals("runtime error: Invalid index.\n", output);
    }

    @Test
    public void array_assignment() throws IOException {
        String output= testFile(testDir + "/array/assignment.tis");
        assertEquals("[1, 2, 3]\n[4, 5, 6]\n", output);
    }

    @Test
    public void array_set() throws IOException {
        String output= testFile(testDir + "/array/set.tis");
        assertEquals("4\n[2, 5, 4]\n[string, 5, 4]\n[string, 5, [1, 2, 3]]\n[[2, 3, 4], [5, 6, 7], [8, 9, 10]]\n", output);
    }

    @Test
    public void array_invalid_set() throws IOException {
        String output= testFile(testDir + "/array/invalid_set.tis");
        assertEquals("runtime error: Invalid index.\n", output);
    }

    @Test
    public void array_access_out_of_range() throws IOException {
        String output= testFile(testDir + "/array/access_out_of_range.tis");
        assertEquals("runtime error: Index 4 out of range.\n", output);
    }

    @Test
    public void static_methods_use() throws IOException {
        String output= testFile(testDir + "/static_methods/use.tis");
        assertEquals("foo\nFoo instance\nhi\ntest foo\nfoo\ntest foo\n", output);
    }

    @Test
    public void static_methods_syntax() throws IOException {
        String output= testFile(testDir + "/static_methods/syntax.tis");
        assertEquals("[line 8] Error at 'static': Expected expression.\n[line 16] Error at 'static': Expected expression.\n[line 26] Error at '{': Expected function name.\n", output);
    }

    @Test
    public void static_methods_inheritance() throws IOException {
        String output= testFile(testDir + "/static_methods/inheritance.tis");
        assertEquals("foo\nbarr\n", output);
    }

    @Test
    public void static_methods_invalid_use_of_this() throws IOException {
        String output= testFile(testDir + "/static_methods/invalid_use_of_this.tis");
        assertEquals("runtime error: Undefined variable 'this'.\n", output);
    }

    @Test
    public void static_methods_this() throws IOException {
        String output= testFile(testDir + "/static_methods/this.tis");
        assertEquals("bar\n", output);
    }

    @Test
    public void static_methods_invalid_access() throws IOException {
        String output= testFile(testDir + "/static_methods/invalid_access.tis");
        assertEquals("runtime error: Undefined property 'test'.\n", output);
    }

    @Test
    public void static_methods_invalid_use_of_super() throws IOException {
        String output= testFile(testDir + "/static_methods/invalid_use_of_super.tis");
        assertEquals("runtime error: Invalid use of 'super' outside of an instance method.\n", output);
    }

    @Test
    public void function_expression_use() throws IOException {
        String output= testFile(testDir + "/function_expression/use.tis");
        assertEquals("[<fn anonymous>]\n14\n{a: <fn anonymous>}\n10\n10\n<fn anonymous>\nhello\nhello\n120\n", output);
    }

    @Test
    public void function_expression_syntax() throws IOException {
        String output= testFile(testDir + "/function_expression/syntax.tis");
        assertEquals("[line 1] Error at '(': Expected function name.\n[line 3] Error at '}': Expected expression.\n", output);
    }

    @Test
    public void function_expression_access() throws IOException {
        String output= testFile(testDir + "/function_expression/access.tis");
        assertEquals("<fn anonymous>\n<fn anonymous>\n<fn anonymous>\n<fn c>\n", output);
    }

    @Test
    public void function_expression_call() throws IOException {
        String output= testFile(testDir + "/function_expression/call.tis");
        assertEquals("10\n", output);
    }

    @Test
    public void slice_array() throws IOException {
        String output= testFile(testDir + "/slice/array.tis");
        assertEquals("[2]\n[2, 3]\n[2, 3, 4]\n[2, 3, 4]\n[2, 3, 4]\n[1, 2]\n[1, 2, 3]\n[1, 2, 3, 4]\n[1, 2, 3, 4]\n[1, 2, 3, 4]\n[1, 2, 3, 4]\n[2, 3, 4]\n[1, 2, 3, 4]\n", output);
    }

    @Test
    public void slice_syntax() throws IOException {
        String output= testFile(testDir + "/slice/syntax.tis");
        assertEquals("", output);
    }

    @Test
    public void slice_string() throws IOException {
        String output= testFile(testDir + "/slice/string.tis");
        assertEquals("T\nTe\nes\nst\nTesti\nTestin\nT\nTe\nTes\nTest\nTesti\nTestin\nTesting\nting\nTesting\n", output);
    }

    @Test
    public void slice_invalid() throws IOException {
        String output= testFile(testDir + "/slice/invalid.tis");
        assertEquals("Invalid slice.\nInvalid slice.\nInvalid slice.\nInvalid slice.\nInvalid slice.\nInvalid slice.\n", output);
    }

    @Test
    public void trycatch_use() throws IOException {
        String output= testFile(testDir + "/trycatch/use.tis");
        assertEquals("error\nError: Undefined variable 'x'.\n\tat 'x' [line: 8]\nOperands must be two numbers or two strings.\n", output);
    }

    @Test
    public void trycatch_scope() throws IOException {
        String output= testFile(testDir + "/trycatch/scope.tis");
        assertEquals("Undefined variable 'a'.\nUndefined variable 'b'.\nUndefined variable 'e'.\n", output);
    }

    @Test
    public void trycatch_syntax() throws IOException {
        String output= testFile(testDir + "/trycatch/syntax.tis");
        assertEquals("[line 1] Error at ';': Expected '{' after 'try'.\n[line 3] Error at ';': Expected 'catch' after try block.\n[line 5] Error at ';': Expected '(' after 'catch'.\n[line 7] Error at ')': Expected exception name.\n[line 9] Error at ';': Expected expression.\n", output);
    }

    @Test
    public void trycatch_error() throws IOException {
        String output= testFile(testDir + "/trycatch/error.tis");
        assertEquals("Expected 1 arguments but got 0.\nerror\n", output);
    }

    @Test
    public void if_fun_in_else() throws IOException {
        String output= testFile(testDir + "/if/fun_in_else.tis");
        assertEquals("[line 2] Error at end: Expected ';' after expression.\n", output);
    }

    @Test
    public void if_class_in_else() throws IOException {
        String output= testFile(testDir + "/if/class_in_else.tis");
        assertEquals("[line 2] Error at 'class': Expected expression.\n", output);
    }

    @Test
    public void if_var_in_then() throws IOException {
        String output= testFile(testDir + "/if/var_in_then.tis");
        assertEquals("[line 2] Error at 'var': Expected expression.\n", output);
    }

    @Test
    public void if_truth() throws IOException {
        String output= testFile(testDir + "/if/truth.tis");
        assertEquals("false\nnil\ntrue\n0\nempty\n", output);
    }

    @Test
    public void if_dangling_else() throws IOException {
        String output= testFile(testDir + "/if/dangling_else.tis");
        assertEquals("good\n", output);
    }

    @Test
    public void if_var_in_else() throws IOException {
        String output= testFile(testDir + "/if/var_in_else.tis");
        assertEquals("[line 2] Error at 'var': Expected expression.\n", output);
    }

    @Test
    public void if_else() throws IOException {
        String output= testFile(testDir + "/if/else.tis");
        assertEquals("good\ngood\nblock\n", output);
    }

    @Test
    public void if_fun_in_then() throws IOException {
        String output= testFile(testDir + "/if/fun_in_then.tis");
        assertEquals("[line 2] Error at end: Expected ';' after expression.\n", output);
    }

    @Test
    public void if_class_in_then() throws IOException {
        String output= testFile(testDir + "/if/class_in_then.tis");
        assertEquals("[line 2] Error at 'class': Expected expression.\n", output);
    }

    @Test
    public void if_if() throws IOException {
        String output= testFile(testDir + "/if/if.tis");
        assertEquals("good\nblock\ntrue\n", output);
    }

    @Test
    public void variadic_function_use() throws IOException {
        String output= testFile(testDir + "/variadic_function/use.tis");
        assertEquals("55\n1\n[2, 3, 4, 5, 6, 7, 8, 9, 10]\n55\n", output);
    }

    @Test
    public void variadic_function_syntax() throws IOException {
        String output= testFile(testDir + "/variadic_function/syntax.tis");
        assertEquals("[line 1] Error at '.': Expected parameter name.\n[line 2] Error at '.': Expected parameter name.\n[line 5] Error at 'b': Variadic parameter must be the last parameter.\n[line 6] Error at '...': Only one variadic parameter is allowed.\n", output);
    }

    @Test
    public void variadic_function_zero_arguments() throws IOException {
        String output= testFile(testDir + "/variadic_function/zero_arguments.tis");
        assertEquals("0\n", output);
    }

    @Test
    public void assignment_grouping() throws IOException {
        String output= testFile(testDir + "/assignment/grouping.tis");
        assertEquals("[line 2] Error at '=': Invalid assignment target.\n", output);
    }

    @Test
    public void assignment_associativity() throws IOException {
        String output= testFile(testDir + "/assignment/associativity.tis");
        assertEquals("c\nc\nc\n", output);
    }

    @Test
    public void assignment_to_this() throws IOException {
        String output= testFile(testDir + "/assignment/to_this.tis");
        assertEquals("[line 3] Error at '=': Invalid assignment target.\n", output);
    }

    @Test
    public void assignment_syntax() throws IOException {
        String output= testFile(testDir + "/assignment/syntax.tis");
        assertEquals("var\nvar\n", output);
    }

    @Test
    public void assignment_global() throws IOException {
        String output= testFile(testDir + "/assignment/global.tis");
        assertEquals("before\nafter\narg\narg\n", output);
    }

    @Test
    public void assignment_prefix_operator() throws IOException {
        String output= testFile(testDir + "/assignment/prefix_operator.tis");
        assertEquals("[line 2] Error at '=': Invalid assignment target.\n", output);
    }

    @Test
    public void assignment_infix_operator() throws IOException {
        String output= testFile(testDir + "/assignment/infix_operator.tis");
        assertEquals("[line 3] Error at '=': Invalid assignment target.\n", output);
    }

    @Test
    public void assignment_local() throws IOException {
        String output= testFile(testDir + "/assignment/local.tis");
        assertEquals("before\nafter\narg\narg\n", output);
    }

    @Test
    public void assignment_undefined() throws IOException {
        String output= testFile(testDir + "/assignment/undefined.tis");
        assertEquals("runtime error: Undefined variable 'unknown'.\n", output);
    }

    @Test
    public void spread_array() throws IOException {
        String output= testFile(testDir + "/spread/array.tis");
        assertEquals("[1, 2, 3, 4, 5, 6]\n[1, 2, 3, 4, 5, 6]\n", output);
    }

    @Test
    public void spread_function() throws IOException {
        String output= testFile(testDir + "/spread/function.tis");
        assertEquals("1 2 3 4\n", output);
    }

    @Test
    public void spread_syntax() throws IOException {
        String output= testFile(testDir + "/spread/syntax.tis");
        assertEquals("[line 1] Error at '.': Expected expression.\n[line 4] Error at '...': Expected expression.\n", output);
    }

    @Test
    public void return_after_if() throws IOException {
        String output= testFile(testDir + "/return/after_if.tis");
        assertEquals("ok\n", output);
    }

    @Test
    public void return_at_top_level() throws IOException {
        String output= testFile(testDir + "/return/at_top_level.tis");
        assertEquals("[line 1] Error at 'return': Can't return from top-level code.\n", output);
    }

    @Test
    public void return_after_else() throws IOException {
        String output= testFile(testDir + "/return/after_else.tis");
        assertEquals("ok\n", output);
    }

    @Test
    public void return_in_method() throws IOException {
        String output= testFile(testDir + "/return/in_method.tis");
        assertEquals("ok\n", output);
    }

    @Test
    public void return_after_while() throws IOException {
        String output= testFile(testDir + "/return/after_while.tis");
        assertEquals("ok\n", output);
    }

    @Test
    public void return_in_function() throws IOException {
        String output= testFile(testDir + "/return/in_function.tis");
        assertEquals("ok\n", output);
    }

    @Test
    public void return_return_nil_if_no_value() throws IOException {
        String output= testFile(testDir + "/return/return_nil_if_no_value.tis");
        assertEquals("nil\n", output);
    }

    @Test
    public void function_empty_body() throws IOException {
        String output= testFile(testDir + "/function/empty_body.tis");
        assertEquals("nil\n", output);
    }

    @Test
    public void function_local_mutual_recursion() throws IOException {
        String output= testFile(testDir + "/function/local_mutual_recursion.tis");
        assertEquals("runtime error: Undefined variable 'isOdd'.\n", output);
    }

    @Test
    public void function_missing_comma_in_parameters() throws IOException {
        String output= testFile(testDir + "/function/missing_comma_in_parameters.tis");
        assertEquals("[line 2] Error at 'c': Expected ')' after parameters.\n", output);
    }

    @Test
    public void function_local_recursion() throws IOException {
        String output= testFile(testDir + "/function/local_recursion.tis");
        assertEquals("21\n", output);
    }

    @Test
    public void function_recursion() throws IOException {
        String output= testFile(testDir + "/function/recursion.tis");
        assertEquals("21\n", output);
    }

    @Test
    public void function_body_must_be_block() throws IOException {
        String output= testFile(testDir + "/function/body_must_be_block.tis");
        assertEquals("[line 2] Error at '123': Expected '{' before function body.\n", output);
    }

    @Test
    public void function_nested_call_with_arguments() throws IOException {
        String output= testFile(testDir + "/function/nested_call_with_arguments.tis");
        assertEquals("hello world\n", output);
    }

    @Test
    public void function_missing_arguments() throws IOException {
        String output= testFile(testDir + "/function/missing_arguments.tis");
        assertEquals("runtime error: Expected 2 arguments but got 1.\n", output);
    }

    @Test
    public void function_parameters() throws IOException {
        String output= testFile(testDir + "/function/parameters.tis");
        assertEquals("0\n1\n3\n6\n10\n15\n21\n28\n36\n", output);
    }

    @Test
    public void function_mutual_recursion() throws IOException {
        String output= testFile(testDir + "/function/mutual_recursion.tis");
        assertEquals("true\ntrue\n", output);
    }

    @Test
    public void function_extra_arguments() throws IOException {
        String output= testFile(testDir + "/function/extra_arguments.tis");
        assertEquals("runtime error: Expected 2 arguments but got 4.\n", output);
    }

    @Test
    public void function_print() throws IOException {
        String output= testFile(testDir + "/function/print.tis");
        assertEquals("<fn foo>\n<native fn>\n", output);
    }

    @Test
    public void field_method() throws IOException {
        String output= testFile(testDir + "/field/method.tis");
        assertEquals("got method\narg\n", output);
    }

    @Test
    public void field_call_nonfunction_field() throws IOException {
        String output= testFile(testDir + "/field/call_nonfunction_field.tis");
        assertEquals("runtime error: Can only call functions and classes.\n", output);
    }

    @Test
    public void field_set_on_nil() throws IOException {
        String output= testFile(testDir + "/field/set_on_nil.tis");
        assertEquals("runtime error: Only instances have fields.\n", output);
    }

    @Test
    public void field_get_on_string() throws IOException {
        String output= testFile(testDir + "/field/get_on_string.tis");
        assertEquals("runtime error: Only instances and classes can be accessed through dot notation.\n", output);
    }

    @Test
    public void field_many() throws IOException {
        String output= testFile(testDir + "/field/many.tis");
        assertEquals("apple\napricot\navocado\nbanana\nbilberry\nblackberry\nblackcurrant\nblueberry\nboysenberry\ncantaloupe\ncherimoya\ncherry\nclementine\ncloudberry\ncoconut\ncranberry\ncurrant\ndamson\ndate\ndragonfruit\ndurian\nelderberry\nfeijoa\nfig\ngooseberry\ngrape\ngrapefruit\nguava\nhoneydew\nhuckleberry\njabuticaba\njackfruit\njambul\njujube\njuniper\nkiwifruit\nkumquat\nlemon\nlime\nlongan\nloquat\nlychee\nmandarine\nmango\nmarionberry\nmelon\nmiracle\nmulberry\nnance\nnectarine\nolive\norange\npapaya\npassionfruit\npeach\npear\npersimmon\nphysalis\npineapple\nplantain\nplum\nplumcot\npomegranate\npomelo\nquince\nraisin\nrambutan\nraspberry\nredcurrant\nsalak\nsalmonberry\nsatsuma\nstrawberry\ntamarillo\ntamarind\ntangerine\ntomato\nwatermelon\nyuzu\n", output);
    }

    @Test
    public void field_set_on_function() throws IOException {
        String output= testFile(testDir + "/field/set_on_function.tis");
        assertEquals("runtime error: Only instances have fields.\n", output);
    }

    @Test
    public void field_set_on_bool() throws IOException {
        String output= testFile(testDir + "/field/set_on_bool.tis");
        assertEquals("runtime error: Only instances have fields.\n", output);
    }

    @Test
    public void field_set_on_string() throws IOException {
        String output= testFile(testDir + "/field/set_on_string.tis");
        assertEquals("runtime error: Only instances have fields.\n", output);
    }

    @Test
    public void field_on_instance() throws IOException {
        String output= testFile(testDir + "/field/on_instance.tis");
        assertEquals("bar value\nbaz value\nbar value\nbaz value\n", output);
    }

    @Test
    public void field_get_on_nil() throws IOException {
        String output= testFile(testDir + "/field/get_on_nil.tis");
        assertEquals("runtime error: Only instances and classes can be accessed through dot notation.\n", output);
    }

    @Test
    public void field_set_on_class() throws IOException {
        String output= testFile(testDir + "/field/set_on_class.tis");
        assertEquals("runtime error: Only instances have fields.\n", output);
    }

    @Test
    public void field_method_binds_this() throws IOException {
        String output= testFile(testDir + "/field/method_binds_this.tis");
        assertEquals("foo1\n1\n", output);
    }

    @Test
    public void field_set_on_num() throws IOException {
        String output= testFile(testDir + "/field/set_on_num.tis");
        assertEquals("runtime error: Only instances have fields.\n", output);
    }

    @Test
    public void field_get_on_function() throws IOException {
        String output= testFile(testDir + "/field/get_on_function.tis");
        assertEquals("runtime error: Only instances and classes can be accessed through dot notation.\n", output);
    }

    @Test
    public void field_call_function_field() throws IOException {
        String output= testFile(testDir + "/field/call_function_field.tis");
        assertEquals("bar\n1\n2\n", output);
    }

    @Test
    public void field_set_evaluation_order() throws IOException {
        String output= testFile(testDir + "/field/set_evaluation_order.tis");
        assertEquals("runtime error: Undefined variable 'undefined1'.\n", output);
    }

    @Test
    public void field_get_on_bool() throws IOException {
        String output= testFile(testDir + "/field/get_on_bool.tis");
        assertEquals("runtime error: Only instances and classes can be accessed through dot notation.\n", output);
    }

    @Test
    public void field_get_on_num() throws IOException {
        String output= testFile(testDir + "/field/get_on_num.tis");
        assertEquals("runtime error: Only instances and classes can be accessed through dot notation.\n", output);
    }

    @Test
    public void field_undefined() throws IOException {
        String output= testFile(testDir + "/field/undefined.tis");
        assertEquals("runtime error: Undefined property 'bar'.\n", output);
    }

    @Test
    public void field_get_and_set_method() throws IOException {
        String output= testFile(testDir + "/field/get_and_set_method.tis");
        assertEquals("other\n1\nmethod\n2\n", output);
    }

    @Test
    public void number_nan_equality() throws IOException {
        String output= testFile(testDir + "/number/nan_equality.tis");
        assertEquals("false\ntrue\ntrue\nfalse\n", output);
    }

    @Test
    public void number_literals() throws IOException {
        String output= testFile(testDir + "/number/literals.tis");
        assertEquals("123\n987654\n0\n-0\n123.456\n-0.001\n", output);
    }

    @Test
    public void number_decimal_point_at_eof() throws IOException {
        String output= testFile(testDir + "/number/decimal_point_at_eof.tis");
        assertEquals("[line 2] Error at end: Expected property name after '.'.\n", output);
    }

    @Test
    public void number_leading_dot() throws IOException {
        String output= testFile(testDir + "/number/leading_dot.tis");
        assertEquals("[line 2] Error at '.': Expected expression.\n", output);
    }

    @Test
    public void number_trailing_dot() throws IOException {
        String output= testFile(testDir + "/number/trailing_dot.tis");
        assertEquals("[line 2] Error at ';': Expected property name after '.'.\n", output);
    }

    @Test
    public void map_syntax() throws IOException {
        String output= testFile(testDir + "/map/syntax.tis");
        assertEquals("[line 1] Error at ')': Expected expression.\n[line 2] Error at '}': Expected expression.\n[line 3] Error at ';': Expected expression.\n[line 4] Error at '}': Expected expression.\n", output);
    }

    @Test
    public void map_access() throws IOException {
        String output= testFile(testDir + "/map/access.tis");
        assertEquals("1\n2\n3\n3\n4\n5\n[1, 2, 3]\n", output);
    }

    @Test
    public void map_invalid_access() throws IOException {
        String output= testFile(testDir + "/map/invalid_access.tis");
        assertEquals("Undefined key 'b'.\nInvalid key [1, 2].\nInvalid key <fn f>.\nInvalid key {}.\nInvalid key A.\nInvalid key A instance.\nUndefined property 'a'.\n", output);
    }

    @Test
    public void map_set() throws IOException {
        String output= testFile(testDir + "/map/set.tis");
        assertEquals("2\n3\n4\n5\n6\n7\n[4, 5, 6]\n8\n", output);
    }

    @Test
    public void map_invalid_set() throws IOException {
        String output= testFile(testDir + "/map/invalid_set.tis");
        assertEquals("Invalid key [1, 2].\nInvalid key <fn f>.\nInvalid key {}.\nInvalid key A.\nInvalid key A instance.\n", output);
    }

    @Test
    public void getter_methods_use() throws IOException {
        String output= testFile(testDir + "/getter_methods/use.tis");
        assertEquals("6\n10\n6\n6\nruntime error: Can only call functions and classes.\n", output);
    }

    @Test
    public void getter_methods_syntax() throws IOException {
        String output= testFile(testDir + "/getter_methods/syntax.tis");
        assertEquals("[line 1] Error at '{': Expected ';' after expression.\n[line 3] Error at '}': Expected expression.\n", output);
    }

    @Test
    public void getter_methods_static() throws IOException {
        String output= testFile(testDir + "/getter_methods/static.tis");
        assertEquals("[line 2] Error at 'static': Getters can't be static.\n", output);
    }

    @Test
    public void getter_methods_init() throws IOException {
        String output= testFile(testDir + "/getter_methods/init.tis");
        assertEquals("[line 2] Error at 'init': Getter method's name can't be 'init'.\n", output);
    }

    @Test
    public void call_nil() throws IOException {
        String output= testFile(testDir + "/call/nil.tis");
        assertEquals("runtime error: Can only call functions and classes.\n", output);
    }

    @Test
    public void call_bool() throws IOException {
        String output= testFile(testDir + "/call/bool.tis");
        assertEquals("runtime error: Can only call functions and classes.\n", output);
    }

    @Test
    public void call_num() throws IOException {
        String output= testFile(testDir + "/call/num.tis");
        assertEquals("runtime error: Can only call functions and classes.\n", output);
    }

    @Test
    public void call_string() throws IOException {
        String output= testFile(testDir + "/call/string.tis");
        assertEquals("runtime error: Can only call functions and classes.\n", output);
    }

    @Test
    public void call_object() throws IOException {
        String output= testFile(testDir + "/call/object.tis");
        assertEquals("runtime error: Can only call functions and classes.\n", output);
    }

    @Test
    public void logical_operator_and() throws IOException {
        String output= testFile(testDir + "/logical_operator/and.tis");
        assertEquals("false\n1\nfalse\ntrue\n3\ntrue\nfalse\n", output);
    }

    @Test
    public void logical_operator_or() throws IOException {
        String output= testFile(testDir + "/logical_operator/or.tis");
        assertEquals("1\n1\ntrue\nfalse\nfalse\nfalse\ntrue\n", output);
    }

    @Test
    public void logical_operator_or_truth() throws IOException {
        String output= testFile(testDir + "/logical_operator/or_truth.tis");
        assertEquals("ok\nok\ntrue\n0\ns\n", output);
    }

    @Test
    public void logical_operator_and_truth() throws IOException {
        String output= testFile(testDir + "/logical_operator/and_truth.tis");
        assertEquals("false\nnil\nok\nok\nok\n", output);
    }

    @Test
    public void inheritance_inherit_from_nil() throws IOException {
        String output= testFile(testDir + "/inheritance/inherit_from_nil.tis");
        assertEquals("runtime error: Superclass must be a class.\n", output);
    }

    @Test
    public void inheritance_parenthesized_superclass() throws IOException {
        String output= testFile(testDir + "/inheritance/parenthesized_superclass.tis");
        assertEquals("[line 4] Error at '(': Expected superclass name.\n", output);
    }

    @Test
    public void inheritance_set_fields_from_base_class() throws IOException {
        String output= testFile(testDir + "/inheritance/set_fields_from_base_class.tis");
        assertEquals("foo 1\nfoo 2\nbar 1\nbar 2\nbar 1\nbar 2\n", output);
    }

    @Test
    public void inheritance_inherit_from_function() throws IOException {
        String output= testFile(testDir + "/inheritance/inherit_from_function.tis");
        assertEquals("runtime error: Superclass must be a class.\n", output);
    }

    @Test
    public void inheritance_inherit_from_number() throws IOException {
        String output= testFile(testDir + "/inheritance/inherit_from_number.tis");
        assertEquals("runtime error: Superclass must be a class.\n", output);
    }

    @Test
    public void inheritance_inherit_methods() throws IOException {
        String output= testFile(testDir + "/inheritance/inherit_methods.tis");
        assertEquals("foo\nbar\nbar\n", output);
    }

    @Test
    public void inheritance_constructor() throws IOException {
        String output= testFile(testDir + "/inheritance/constructor.tis");
        assertEquals("value\n", output);
    }

    @Test
    public void super_call_same_method() throws IOException {
        String output= testFile(testDir + "/super/call_same_method.tis");
        assertEquals("Derived.foo()\nBase.foo()\n", output);
    }

    @Test
    public void super_no_superclass_call() throws IOException {
        String output= testFile(testDir + "/super/no_superclass_call.tis");
        assertEquals("[line 3] Error at 'super': Can't use 'super' in a class with no superclass.\n", output);
    }

    @Test
    public void super_no_superclass_method() throws IOException {
        String output= testFile(testDir + "/super/no_superclass_method.tis");
        assertEquals("runtime error: Undefined property 'doesNotExist'.\n", output);
    }

    @Test
    public void super_no_superclass_bind() throws IOException {
        String output= testFile(testDir + "/super/no_superclass_bind.tis");
        assertEquals("[line 3] Error at 'super': Can't use 'super' in a class with no superclass.\n", output);
    }

    @Test
    public void super_parenthesized() throws IOException {
        String output= testFile(testDir + "/super/parenthesized.tis");
        assertEquals("[line 8] Error at ')': Expected '.' after 'super'.\n", output);
    }

    @Test
    public void super_this_in_superclass_method() throws IOException {
        String output= testFile(testDir + "/super/this_in_superclass_method.tis");
        assertEquals("a\nb\n", output);
    }

    @Test
    public void super_closure() throws IOException {
        String output= testFile(testDir + "/super/closure.tis");
        assertEquals("Base\n", output);
    }

    @Test
    public void super_super_in_closure_in_inherited_method() throws IOException {
        String output= testFile(testDir + "/super/super_in_closure_in_inherited_method.tis");
        assertEquals("A\n", output);
    }

    @Test
    public void super_super_in_inherited_method() throws IOException {
        String output= testFile(testDir + "/super/super_in_inherited_method.tis");
        assertEquals("A\n", output);
    }

    @Test
    public void super_super_without_dot() throws IOException {
        String output= testFile(testDir + "/super/super_without_dot.tis");
        assertEquals("[line 6] Error at ';': Expected '.' after 'super'.\n", output);
    }

    @Test
    public void super_super_in_top_level_function() throws IOException {
        String output= testFile(testDir + "/super/super_in_top_level_function.tis");
        assertEquals("[line 1] Error at 'super': Can't use 'super' outside of a class.\n", output);
    }

    @Test
    public void super_call_other_method() throws IOException {
        String output= testFile(testDir + "/super/call_other_method.tis");
        assertEquals("Derived.bar()\nBase.foo()\n", output);
    }

    @Test
    public void super_missing_arguments() throws IOException {
        String output= testFile(testDir + "/super/missing_arguments.tis");
        assertEquals("runtime error: Expected 2 arguments but got 1.\n", output);
    }

    @Test
    public void super_super_without_name() throws IOException {
        String output= testFile(testDir + "/super/super_without_name.tis");
        assertEquals("[line 5] Error at ';': Expected superclass method name.\n", output);
    }

    @Test
    public void super_bound_method() throws IOException {
        String output= testFile(testDir + "/super/bound_method.tis");
        assertEquals("A.method(arg)\n", output);
    }

    @Test
    public void super_extra_arguments() throws IOException {
        String output= testFile(testDir + "/super/extra_arguments.tis");
        assertEquals("Derived.foo()\nruntime error: Expected 2 arguments but got 4.\n", output);
    }

    @Test
    public void super_constructor() throws IOException {
        String output= testFile(testDir + "/super/constructor.tis");
        assertEquals("Derived.init()\nBase.init(a, b)\n", output);
    }

    @Test
    public void super_reassign_superclass() throws IOException {
        String output= testFile(testDir + "/super/reassign_superclass.tis");
        assertEquals("Base.method()\nBase.method()\n", output);
    }

    @Test
    public void super_indirectly_inherited() throws IOException {
        String output= testFile(testDir + "/super/indirectly_inherited.tis");
        assertEquals("C.foo()\nA.foo()\n", output);
    }

    @Test
    public void super_super_at_top_level() throws IOException {
        String output= testFile(testDir + "/super/super_at_top_level.tis");
        assertEquals("[line 1] Error at 'super': Can't use 'super' outside of a class.\n[line 2] Error at 'super': Can't use 'super' outside of a class.\n", output);
    }

    @Test
    public void bool_equality() throws IOException {
        String output= testFile(testDir + "/bool/equality.tis");
        assertEquals("true\nfalse\nfalse\ntrue\nfalse\nfalse\nfalse\nfalse\nfalse\nfalse\ntrue\ntrue\nfalse\ntrue\ntrue\ntrue\ntrue\ntrue\n", output);
    }

    @Test
    public void bool_not() throws IOException {
        String output= testFile(testDir + "/bool/not.tis");
        assertEquals("false\ntrue\ntrue\n", output);
    }

    @Test
    public void for_var_in_body() throws IOException {
        String output= testFile(testDir + "/for/var_in_body.tis");
        assertEquals("[line 2] Error at 'var': Expected expression.\n", output);
    }

    @Test
    public void for_return_closure() throws IOException {
        String output= testFile(testDir + "/for/return_closure.tis");
        assertEquals("i\n", output);
    }

    @Test
    public void for_scope() throws IOException {
        String output= testFile(testDir + "/for/scope.tis");
        assertEquals("0\n-1\nafter\n0\n", output);
    }

    @Test
    public void for_syntax() throws IOException {
        String output= testFile(testDir + "/for/syntax.tis");
        assertEquals("1\n2\n3\n0\n1\n2\ndone\n0\n1\n0\n1\n2\n0\n1\n", output);
    }

    @Test
    public void for_return_inside() throws IOException {
        String output= testFile(testDir + "/for/return_inside.tis");
        assertEquals("i\n", output);
    }

    @Test
    public void for_statement_condition() throws IOException {
        String output= testFile(testDir + "/for/statement_condition.tis");
        assertEquals("[line 3] Error at 'if': Expected expression.\n[line 3] Error at ')': Expected ';' after expression.\n", output);
    }

    @Test
    public void for_statement_initializer() throws IOException {
        String output= testFile(testDir + "/for/statement_initializer.tis");
        assertEquals("[line 3] Error at 'if': Expected expression.\n[line 3] Error at ')': Expected ';' after expression.\n", output);
    }

    @Test
    public void for_statement_increment() throws IOException {
        String output= testFile(testDir + "/for/statement_increment.tis");
        assertEquals("[line 2] Error at 'if': Expected expression.\n", output);
    }

    @Test
    public void for_closure_in_body() throws IOException {
        String output= testFile(testDir + "/for/closure_in_body.tis");
        assertEquals("4\n1\n4\n2\n4\n3\n", output);
    }

    @Test
    public void for_class_in_body() throws IOException {
        String output= testFile(testDir + "/for/class_in_body.tis");
        assertEquals("[line 2] Error at 'class': Expected expression.\n", output);
    }

    @Test
    public void for_fun_in_body() throws IOException {
        String output= testFile(testDir + "/for/fun_in_body.tis");
        assertEquals("[line 2] Error at end: Expected ';' after expression.\n", output);
    }

    @Test
    public void switch_use() throws IOException {
        String output= testFile(testDir + "/switch/use.tis");
        assertEquals("1\n2\n3\ndefault\n1\n2\n3\ndefault\n1\n2\n3\n2\n3\n3\n1\n2\n2\ndefault\n1\n2\n1\nnil\n", output);
    }

    @Test
    public void switch_switch_on_complex_data_types() throws IOException {
        String output= testFile(testDir + "/switch/switch_on_complex_data_types.tis");
        assertEquals("default\ndefault\n1,2,3\ndefault\nFoo\n", output);
    }

    @Test
    public void switch_scope() throws IOException {
        String output= testFile(testDir + "/switch/scope.tis");
        assertEquals("2\n6\nruntime error: Undefined variable 'y'.\n", output);
    }

    @Test
    public void switch_syntax() throws IOException {
        String output= testFile(testDir + "/switch/syntax.tis");
        assertEquals("[line 1] Error at '{': Expected '('.\n[line 3] Error at 'x': Expected '{'.\n[line 6] Error at ':': Expected expression.\n[line 11] Error at '3': Expected ':'.\n[line 13] Error at ')': Expected expression.\n[line 15] Error at '{': Expected ')'.\n[line 18] Error at 'break': Expected 'case' or 'default'.\n[line 25] Error at 'default': Duplicate use of 'default'.\n", output);
    }

    @Test
    public void switch_switch_expr_evaluation() throws IOException {
        String output= testFile(testDir + "/switch/switch_expr_evaluation.tis");
        assertEquals("1\n1\n1\n", output);
    }

    @Test
    public void switch_case_expr_evaluation_order() throws IOException {
        String output= testFile(testDir + "/switch/case_expr_evaluation_order.tis");
        assertEquals("1\n1\n2\n1\n2\n3\ndefault\n1\n2\n3\n1\n1\n2\n3\n1\nruntime error: Undefined variable 'd'.\n", output);
    }

    @Test
    public void class_empty() throws IOException {
        String output= testFile(testDir + "/class/empty.tis");
        assertEquals("Foo\n", output);
    }

    @Test
    public void class_local_inherit_self() throws IOException {
        String output= testFile(testDir + "/class/local_inherit_self.tis");
        assertEquals("[line 2] Error at 'Foo': A class can't inherit from itself.\n", output);
    }

    @Test
    public void class_local_inherit_other() throws IOException {
        String output= testFile(testDir + "/class/local_inherit_other.tis");
        assertEquals("B\n", output);
    }

    @Test
    public void class_reference_self() throws IOException {
        String output= testFile(testDir + "/class/reference_self.tis");
        assertEquals("Foo\n", output);
    }

    @Test
    public void class_inherited_method() throws IOException {
        String output= testFile(testDir + "/class/inherited_method.tis");
        assertEquals("in foo\nin bar\nin baz\n", output);
    }

    @Test
    public void class_local_reference_self() throws IOException {
        String output= testFile(testDir + "/class/local_reference_self.tis");
        assertEquals("Foo\n", output);
    }

    @Test
    public void class_inherit_self() throws IOException {
        String output= testFile(testDir + "/class/inherit_self.tis");
        assertEquals("[line 1] Error at 'Foo': A class can't inherit from itself.\n", output);
    }

    @Test
    public void this_this_in_method() throws IOException {
        String output= testFile(testDir + "/this/this_in_method.tis");
        assertEquals("baz\n", output);
    }

    @Test
    public void this_closure() throws IOException {
        String output= testFile(testDir + "/this/closure.tis");
        assertEquals("Foo\n", output);
    }

    @Test
    public void this_this_at_top_level() throws IOException {
        String output= testFile(testDir + "/this/this_at_top_level.tis");
        assertEquals("[line 1] Error at 'this': Can't use 'this' outside of a class.\n", output);
    }

    @Test
    public void this_nested_class() throws IOException {
        String output= testFile(testDir + "/this/nested_class.tis");
        assertEquals("Outer instance\nOuter instance\nInner instance\n", output);
    }

    @Test
    public void this_this_in_top_level_function() throws IOException {
        String output= testFile(testDir + "/this/this_in_top_level_function.tis");
        assertEquals("[line 2] Error at 'this': Can't use 'this' outside of a class.\n", output);
    }

    @Test
    public void this_nested_closure() throws IOException {
        String output= testFile(testDir + "/this/nested_closure.tis");
        assertEquals("Foo\n", output);
    }

    @Test
    public void compound_assignment_power() throws IOException {
        String output= testFile(testDir + "/compound_assignment/power.tis");
        assertEquals("8\nOperands must be numbers.\n", output);
    }

    @Test
    public void compound_assignment_power_syntax() throws IOException {
        String output= testFile(testDir + "/compound_assignment/power_syntax.tis");
        assertEquals("[line 1] Error at '**=': Invalid assignment target.\n", output);
    }

    @Test
    public void string_with_esacape_characters() throws IOException {
        String output= testFile(testDir + "/string/with_esacape_characters.tis");
        assertEquals("", output);
    }

    @Test
    public void string_literals() throws IOException {
        String output= testFile(testDir + "/string/literals.tis");
        assertEquals("()\na string\nA~¶Þॐஃ\n", output);
    }

    @Test
    public void string_print_with_escap_characters() throws IOException {
        String output= testFile(testDir + "/string/print_with_escap_characters.tis");
        assertEquals("H\ne\rll\to\b,\f \"World\"\n", output);
    }

    @Test
    public void string_error_after_multiline() throws IOException {
        String output= testFile(testDir + "/string/error_after_multiline.tis");
        assertEquals("runtime error: Undefined variable 'err'.\n", output);
    }

    @Test
    public void string_unicode() throws IOException {
        String output= testFile(testDir + "/string/unicode.tis");
        assertEquals("", output);
    }

    @Test
    public void string_invalid_unicode() throws IOException {
        String output= testFile(testDir + "/string/invalid_unicode.tis");
        assertEquals("[line 1] Error: Invalid unicode sequence.\n", output);
    }

    @Test
    public void string_multiline() throws IOException {
        String output= testFile(testDir + "/string/multiline.tis");
        assertEquals("1\n2\n3\n", output);
    }

    @Test
    public void string_invalid_unicode_length() throws IOException {
        String output= testFile(testDir + "/string/invalid_unicode_length.tis");
        assertEquals("[line 1] Error: Invalid unicode sequence.\n", output);
    }

    @Test
    public void string_unterminated_with_escaping() throws IOException {
        String output= testFile(testDir + "/string/unterminated_with_escaping.tis");
        assertEquals("[line 1] Error: Unterminated string.\n", output);
    }

    @Test
    public void string_invalid_escaping() throws IOException {
        String output= testFile(testDir + "/string/invalid_escaping.tis");
        assertEquals("[line 1] Error: Invalid escape character.\n", output);
    }

    @Test
    public void string_print_unicode() throws IOException {
        String output= testFile(testDir + "/string/print_unicode.tis");
        assertEquals("Money₹\n", output);
    }

    @Test
    public void string_unterminated() throws IOException {
        String output= testFile(testDir + "/string/unterminated.tis");
        assertEquals("[line 2] Error: Unterminated string.\n", output);
    }

    @Test
    public void break_nested() throws IOException {
        String output= testFile(testDir + "/break/nested.tis");
        assertEquals("45\n9\n", output);
    }

    @Test
    public void break_for() throws IOException {
        String output= testFile(testDir + "/break/for.tis");
        assertEquals("5\n", output);
    }

    @Test
    public void break_while() throws IOException {
        String output= testFile(testDir + "/break/while.tis");
        assertEquals("5\n", output);
    }

    @Test
    public void break_invalid() throws IOException {
        String output= testFile(testDir + "/break/invalid.tis");
        assertEquals("[line 1] Error at 'break': Can't use 'break' outside of a loop, switch.\n[line 4] Error at 'break': Can't use 'break' outside of a loop, switch.\n", output);
    }

    @Test
    public void regression_394() throws IOException {
        String output= testFile(testDir + "/regression/394.tis");
        assertEquals("B\n", output);
    }

    @Test
    public void regression_40() throws IOException {
        String output= testFile(testDir + "/regression/40.tis");
        assertEquals("false\n", output);
    }

    @Test
    public void continue_nested() throws IOException {
        String output= testFile(testDir + "/continue/nested.tis");
        assertEquals("90\n10\n", output);
    }

    @Test
    public void continue_for() throws IOException {
        String output= testFile(testDir + "/continue/for.tis");
        assertEquals("9\n8\n10\n", output);
    }

    @Test
    public void continue_while() throws IOException {
        String output= testFile(testDir + "/continue/while.tis");
        assertEquals("9\n8\n10\n", output);
    }

    @Test
    public void continue_invalid() throws IOException {
        String output= testFile(testDir + "/continue/invalid.tis");
        assertEquals("[line 1] Error at 'continue': Can't use 'continue' outside of a loop.\n[line 4] Error at 'continue': Can't use 'continue' outside of a loop.\n[line 9] Error at 'continue': Can't use 'continue' outside of a loop.\n", output);
    }

    @Test
    public void while_var_in_body() throws IOException {
        String output= testFile(testDir + "/while/var_in_body.tis");
        assertEquals("[line 2] Error at 'var': Expected expression.\n", output);
    }

    @Test
    public void while_return_closure() throws IOException {
        String output= testFile(testDir + "/while/return_closure.tis");
        assertEquals("i\n", output);
    }

    @Test
    public void while_syntax() throws IOException {
        String output= testFile(testDir + "/while/syntax.tis");
        assertEquals("1\n2\n3\n0\n1\n2\n", output);
    }

    @Test
    public void while_return_inside() throws IOException {
        String output= testFile(testDir + "/while/return_inside.tis");
        assertEquals("i\n", output);
    }

    @Test
    public void while_closure_in_body() throws IOException {
        String output= testFile(testDir + "/while/closure_in_body.tis");
        assertEquals("1\n2\n3\n", output);
    }

    @Test
    public void while_class_in_body() throws IOException {
        String output= testFile(testDir + "/while/class_in_body.tis");
        assertEquals("[line 2] Error at 'class': Expected expression.\n", output);
    }

    @Test
    public void while_fun_in_body() throws IOException {
        String output= testFile(testDir + "/while/fun_in_body.tis");
        assertEquals("[line 2] Error at end: Expected ';' after expression.\n", output);
    }

    @Test
    public void method_empty_block() throws IOException {
        String output= testFile(testDir + "/method/empty_block.tis");
        assertEquals("nil\n", output);
    }

    @Test
    public void method_refer_to_name() throws IOException {
        String output= testFile(testDir + "/method/refer_to_name.tis");
        assertEquals("runtime error: Undefined variable 'method'.\n", output);
    }

    @Test
    public void method_arity() throws IOException {
        String output= testFile(testDir + "/method/arity.tis");
        assertEquals("no args\n1\n3\n6\n10\n15\n21\n28\n36\n", output);
    }

    @Test
    public void method_print_bound_method() throws IOException {
        String output= testFile(testDir + "/method/print_bound_method.tis");
        assertEquals("<fn method>\n", output);
    }

    @Test
    public void method_not_found() throws IOException {
        String output= testFile(testDir + "/method/not_found.tis");
        assertEquals("runtime error: Undefined property 'unknown'.\n", output);
    }

    @Test
    public void method_missing_arguments() throws IOException {
        String output= testFile(testDir + "/method/missing_arguments.tis");
        assertEquals("runtime error: Expected 2 arguments but got 1.\n", output);
    }

    @Test
    public void method_extra_arguments() throws IOException {
        String output= testFile(testDir + "/method/extra_arguments.tis");
        assertEquals("runtime error: Expected 2 arguments but got 4.\n", output);
    }

    @Test
    public void precedence() throws IOException {
        String output= testFile(testDir + "/precedence.tis");
        assertEquals("14\n8\n4\n0\ntrue\ntrue\ntrue\ntrue\n0\n0\n0\n0\n-4\n4\n", output);
    }

    @Test
    public void operator_divide_nonnum_num() throws IOException {
        String output= testFile(testDir + "/operator/divide_nonnum_num.tis");
        assertEquals("runtime error: Operands must be numbers.\n", output);
    }

    @Test
    public void operator_comparison() throws IOException {
        String output= testFile(testDir + "/operator/comparison.tis");
        assertEquals("true\nfalse\nfalse\ntrue\ntrue\nfalse\nfalse\nfalse\ntrue\nfalse\ntrue\ntrue\nfalse\nfalse\nfalse\nfalse\ntrue\ntrue\ntrue\ntrue\n", output);
    }

    @Test
    public void operator_multiply_nonnum_num() throws IOException {
        String output= testFile(testDir + "/operator/multiply_nonnum_num.tis");
        assertEquals("runtime error: Operands must be numbers.\n", output);
    }

    @Test
    public void operator_greater_num_nonnum() throws IOException {
        String output= testFile(testDir + "/operator/greater_num_nonnum.tis");
        assertEquals("runtime error: Operands must be numbers.\n", output);
    }

    @Test
    public void operator_less_or_equal_nonnum_num() throws IOException {
        String output= testFile(testDir + "/operator/less_or_equal_nonnum_num.tis");
        assertEquals("runtime error: Operands must be numbers.\n", output);
    }

    @Test
    public void operator_not_equals() throws IOException {
        String output= testFile(testDir + "/operator/not_equals.tis");
        assertEquals("false\nfalse\ntrue\nfalse\ntrue\nfalse\ntrue\ntrue\ntrue\ntrue\n", output);
    }

    @Test
    public void operator_add_bool_num() throws IOException {
        String output= testFile(testDir + "/operator/add_bool_num.tis");
        assertEquals("runtime error: Operands must be two numbers or two strings.\n", output);
    }

    @Test
    public void operator_add_num_nil() throws IOException {
        String output= testFile(testDir + "/operator/add_num_nil.tis");
        assertEquals("runtime error: Operands must be two numbers or two strings.\n", output);
    }

    @Test
    public void operator_power() throws IOException {
        String output= testFile(testDir + "/operator/power.tis");
        assertEquals("1\n8\n0.5\n0.25\n0\n-4\n0.25\n", output);
    }

    @Test
    public void operator_equals_method() throws IOException {
        String output= testFile(testDir + "/operator/equals_method.tis");
        assertEquals("true\nfalse\n", output);
    }

    @Test
    public void operator_equals_class() throws IOException {
        String output= testFile(testDir + "/operator/equals_class.tis");
        assertEquals("true\nfalse\nfalse\ntrue\nfalse\nfalse\nfalse\nfalse\n", output);
    }

    @Test
    public void operator_subtract_num_nonnum() throws IOException {
        String output= testFile(testDir + "/operator/subtract_num_nonnum.tis");
        assertEquals("runtime error: Operands must be numbers.\n", output);
    }

    @Test
    public void operator_multiply() throws IOException {
        String output= testFile(testDir + "/operator/multiply.tis");
        assertEquals("15\n3.702\n", output);
    }

    @Test
    public void operator_negate() throws IOException {
        String output= testFile(testDir + "/operator/negate.tis");
        assertEquals("-3\n", output);
    }

    @Test
    public void operator_divide() throws IOException {
        String output= testFile(testDir + "/operator/divide.tis");
        assertEquals("4\n1\n", output);
    }

    @Test
    public void operator_add_string_nil() throws IOException {
        String output= testFile(testDir + "/operator/add_string_nil.tis");
        assertEquals("runtime error: Operands must be two numbers or two strings.\n", output);
    }

    @Test
    public void operator_add() throws IOException {
        String output= testFile(testDir + "/operator/add.tis");
        assertEquals("579\nstring\n", output);
    }

    @Test
    public void operator_greater_or_equal_nonnum_num() throws IOException {
        String output= testFile(testDir + "/operator/greater_or_equal_nonnum_num.tis");
        assertEquals("runtime error: Operands must be numbers.\n", output);
    }

    @Test
    public void operator_negate_nonnum() throws IOException {
        String output= testFile(testDir + "/operator/negate_nonnum.tis");
        assertEquals("runtime error: Operand must be a number.\n", output);
    }

    @Test
    public void operator_equals() throws IOException {
        String output= testFile(testDir + "/operator/equals.tis");
        assertEquals("true\ntrue\nfalse\ntrue\nfalse\ntrue\nfalse\nfalse\nfalse\nfalse\n", output);
    }

    @Test
    public void operator_less_nonnum_num() throws IOException {
        String output= testFile(testDir + "/operator/less_nonnum_num.tis");
        assertEquals("runtime error: Operands must be numbers.\n", output);
    }

    @Test
    public void operator_add_bool_string() throws IOException {
        String output= testFile(testDir + "/operator/add_bool_string.tis");
        assertEquals("runtime error: Operands must be two numbers or two strings.\n", output);
    }

    @Test
    public void operator_not() throws IOException {
        String output= testFile(testDir + "/operator/not.tis");
        assertEquals("false\ntrue\ntrue\nfalse\nfalse\ntrue\nfalse\nfalse\n", output);
    }

    @Test
    public void operator_add_nil_nil() throws IOException {
        String output= testFile(testDir + "/operator/add_nil_nil.tis");
        assertEquals("runtime error: Operands must be two numbers or two strings.\n", output);
    }

    @Test
    public void operator_subtract() throws IOException {
        String output= testFile(testDir + "/operator/subtract.tis");
        assertEquals("1\n0\n", output);
    }

    @Test
    public void operator_subtract_nonnum_num() throws IOException {
        String output= testFile(testDir + "/operator/subtract_nonnum_num.tis");
        assertEquals("runtime error: Operands must be numbers.\n", output);
    }

    @Test
    public void operator_add_bool_nil() throws IOException {
        String output= testFile(testDir + "/operator/add_bool_nil.tis");
        assertEquals("runtime error: Operands must be two numbers or two strings.\n", output);
    }

    @Test
    public void operator_divide_num_nonnum() throws IOException {
        String output= testFile(testDir + "/operator/divide_num_nonnum.tis");
        assertEquals("runtime error: Operands must be numbers.\n", output);
    }

    @Test
    public void operator_less_or_equal_num_nonnum() throws IOException {
        String output= testFile(testDir + "/operator/less_or_equal_num_nonnum.tis");
        assertEquals("runtime error: Operands must be numbers.\n", output);
    }

    @Test
    public void operator_greater_nonnum_num() throws IOException {
        String output= testFile(testDir + "/operator/greater_nonnum_num.tis");
        assertEquals("runtime error: Operands must be numbers.\n", output);
    }

    @Test
    public void operator_multiply_num_nonnum() throws IOException {
        String output= testFile(testDir + "/operator/multiply_num_nonnum.tis");
        assertEquals("runtime error: Operands must be numbers.\n", output);
    }

    @Test
    public void operator_greater_or_equal_num_nonnum() throws IOException {
        String output= testFile(testDir + "/operator/greater_or_equal_num_nonnum.tis");
        assertEquals("runtime error: Operands must be numbers.\n", output);
    }

    @Test
    public void operator_power_zero_to_negative_power() throws IOException {
        String output= testFile(testDir + "/operator/power_zero_to_negative_power.tis");
        assertEquals("runtime error: zero cannot be raised to negative power.\n", output);
    }

    @Test
    public void operator_power_nonnum() throws IOException {
        String output= testFile(testDir + "/operator/power_nonnum.tis");
        assertEquals("Operands must be numbers.\nOperands must be numbers.\nOperands must be numbers.\n", output);
    }

    @Test
    public void operator_less_num_nonnum() throws IOException {
        String output= testFile(testDir + "/operator/less_num_nonnum.tis");
        assertEquals("runtime error: Operands must be numbers.\n", output);
    }

    @Test
    public void operator_not_class() throws IOException {
        String output= testFile(testDir + "/operator/not_class.tis");
        assertEquals("false\nfalse\n", output);
    }

    @Test
    public void constructor_return_value() throws IOException {
        String output= testFile(testDir + "/constructor/return_value.tis");
        assertEquals("[line 3] Error at 'return': Can't return a value from an initializer.\n", output);
    }

    @Test
    public void constructor_call_init_explicitly() throws IOException {
        String output= testFile(testDir + "/constructor/call_init_explicitly.tis");
        assertEquals("Foo.init(one)\nFoo.init(two)\nFoo instance\ninit\n", output);
    }

    @Test
    public void constructor_init_not_method() throws IOException {
        String output= testFile(testDir + "/constructor/init_not_method.tis");
        assertEquals("not initializer\n", output);
    }

    @Test
    public void constructor_default() throws IOException {
        String output= testFile(testDir + "/constructor/default.tis");
        assertEquals("Foo instance\n", output);
    }

    @Test
    public void constructor_arguments() throws IOException {
        String output= testFile(testDir + "/constructor/arguments.tis");
        assertEquals("init\n1\n2\n", output);
    }

    @Test
    public void constructor_missing_arguments() throws IOException {
        String output= testFile(testDir + "/constructor/missing_arguments.tis");
        assertEquals("runtime error: Expected 2 arguments but got 1.\n", output);
    }

    @Test
    public void constructor_return_in_nested_function() throws IOException {
        String output= testFile(testDir + "/constructor/return_in_nested_function.tis");
        assertEquals("bar\nFoo instance\n", output);
    }

    @Test
    public void constructor_extra_arguments() throws IOException {
        String output= testFile(testDir + "/constructor/extra_arguments.tis");
        assertEquals("runtime error: Expected 2 arguments but got 4.\n", output);
    }

    @Test
    public void constructor_early_return() throws IOException {
        String output= testFile(testDir + "/constructor/early_return.tis");
        assertEquals("init\nFoo instance\n", output);
    }

    @Test
    public void constructor_default_arguments() throws IOException {
        String output= testFile(testDir + "/constructor/default_arguments.tis");
        assertEquals("runtime error: Expected 0 arguments but got 3.\n", output);
    }

    @Test
    public void constructor_call_init_early_return() throws IOException {
        String output= testFile(testDir + "/constructor/call_init_early_return.tis");
        assertEquals("init\ninit\nFoo instance\n", output);
    }

    @Test
    public void block_empty() throws IOException {
        String output= testFile(testDir + "/block/empty.tis");
        assertEquals("ok\n", output);
    }

    @Test
    public void block_scope() throws IOException {
        String output= testFile(testDir + "/block/scope.tis");
        assertEquals("inner\nouter\n", output);
    }

}
