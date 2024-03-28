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
    private static final String testDir = "src/test/java/com/sravan/lox/testcases";

    private static final void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        Lox.run(new String(bytes, Charset.defaultCharset()));
    }

    @Rule
    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();

    @Rule
    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();

    @Test
    public void closure_reuse_closure_slot() throws IOException {

        runFile(testDir + "/closure/reuse_closure_slot.lox");
        String result = systemOutRule.getLog();
        assertEquals("a\n", result);
    }

    @Test
    public void closure_assign_to_shadowed_later() throws IOException {

        runFile(testDir + "/closure/assign_to_shadowed_later.lox");
        String result = systemOutRule.getLog();
        assertEquals("inner\nassigned\n", result);
    }

    @Test
    public void closure_close_over_later_variable() throws IOException {

        runFile(testDir + "/closure/close_over_later_variable.lox");
        String result = systemOutRule.getLog();
        assertEquals("b\na\n", result);
    }

    @Test
    public void closure_closed_closure_in_function() throws IOException {

        runFile(testDir + "/closure/closed_closure_in_function.lox");
        String result = systemOutRule.getLog();
        assertEquals("local\n", result);
    }

    @Test
    public void closure_unused_later_closure() throws IOException {

        runFile(testDir + "/closure/unused_later_closure.lox");
        String result = systemOutRule.getLog();
        assertEquals("a\n", result);
    }

    @Test
    public void closure_shadow_closure_with_local() throws IOException {

        runFile(testDir + "/closure/shadow_closure_with_local.lox");
        String result = systemOutRule.getLog();
        assertEquals("closure\nshadow\nclosure\n", result);
    }

    @Test
    public void closure_unused_closure() throws IOException {

        runFile(testDir + "/closure/unused_closure.lox");
        String result = systemOutRule.getLog();
        assertEquals("ok\n", result);
    }

    @Test
    public void closure_close_over_function_parameter() throws IOException {

        runFile(testDir + "/closure/close_over_function_parameter.lox");
        String result = systemOutRule.getLog();
        assertEquals("param\n", result);
    }

    @Test
    public void closure_close_over_method_parameter() throws IOException {

        runFile(testDir + "/closure/close_over_method_parameter.lox");
        String result = systemOutRule.getLog();
        assertEquals("param\n", result);
    }

    @Test
    public void closure_open_closure_in_function() throws IOException {

        runFile(testDir + "/closure/open_closure_in_function.lox");
        String result = systemOutRule.getLog();
        assertEquals("local\n", result);
    }

    @Test
    public void closure_reference_closure_multiple_times() throws IOException {

        runFile(testDir + "/closure/reference_closure_multiple_times.lox");
        String result = systemOutRule.getLog();
        assertEquals("a\na\n", result);
    }

    @Test
    public void closure_nested_closure() throws IOException {

        runFile(testDir + "/closure/nested_closure.lox");
        String result = systemOutRule.getLog();
        assertEquals("a\nb\nc\n", result);
    }

    @Test
    public void closure_assign_to_closure() throws IOException {

        runFile(testDir + "/closure/assign_to_closure.lox");
        String result = systemOutRule.getLog();
        assertEquals("local\nafter f\nafter f\nafter g\n", result);
    }

    @Test
    public void map() throws IOException {

        runFile(testDir + "/map.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void comments_line_at_eof() throws IOException {

        runFile(testDir + "/comments/line_at_eof.lox");
        String result = systemOutRule.getLog();
        assertEquals("ok\n", result);
    }

    @Test
    public void comments_only_line_comment() throws IOException {

        runFile(testDir + "/comments/only_line_comment.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void comments_unicode() throws IOException {

        runFile(testDir + "/comments/unicode.lox");
        String result = systemOutRule.getLog();
        assertEquals("ok\n", result);
    }

    @Test
    public void comments_only_line_comment_and_line() throws IOException {

        runFile(testDir + "/comments/only_line_comment_and_line.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void break_stmt() throws IOException {

        runFile(testDir + "/break_stmt.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void continue_stmt() throws IOException {

        runFile(testDir + "/continue_stmt.lox");
        String result = systemOutRule.getLog();
        assertEquals("Test passed!\n", result);
    }

    @Test
    public void empty_file() throws IOException {

        runFile(testDir + "/empty_file.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void try_catch() throws IOException {

        runFile(testDir + "/try_catch.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void variable_in_nested_block() throws IOException {

        runFile(testDir + "/variable/in_nested_block.lox");
        String result = systemOutRule.getLog();
        assertEquals("outer\n", result);
    }

    @Test
    public void variable_scope_reuse_in_different_blocks() throws IOException {

        runFile(testDir + "/variable/scope_reuse_in_different_blocks.lox");
        String result = systemOutRule.getLog();
        assertEquals("first\nsecond\n", result);
    }

    @Test
    public void variable_local_from_method() throws IOException {

        runFile(testDir + "/variable/local_from_method.lox");
        String result = systemOutRule.getLog();
        assertEquals("variable\n", result);
    }

    @Test
    public void variable_use_global_in_initializer() throws IOException {

        runFile(testDir + "/variable/use_global_in_initializer.lox");
        String result = systemOutRule.getLog();
        assertEquals("value\n", result);
    }

    @Test
    public void variable_use_this_as_var() throws IOException {

        runFile(testDir + "/variable/use_this_as_var.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void variable_redeclare_global() throws IOException {

        runFile(testDir + "/variable/redeclare_global.lox");
        String result = systemOutRule.getLog();
        assertEquals("nil\n", result);
    }

    @Test
    public void variable_use_nil_as_var() throws IOException {

        runFile(testDir + "/variable/use_nil_as_var.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void variable_undefined_global() throws IOException {

        runFile(testDir + "/variable/undefined_global.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void variable_shadow_and_local() throws IOException {

        runFile(testDir + "/variable/shadow_and_local.lox");
        String result = systemOutRule.getLog();
        assertEquals("outer\ninner\n", result);
    }

    @Test
    public void variable_early_bound() throws IOException {

        runFile(testDir + "/variable/early_bound.lox");
        String result = systemOutRule.getLog();
        assertEquals("outer\nouter\n", result);
    }

    @Test
    public void variable_duplicate_parameter() throws IOException {

        runFile(testDir + "/variable/duplicate_parameter.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void variable_uninitialized() throws IOException {

        runFile(testDir + "/variable/uninitialized.lox");
        String result = systemOutRule.getLog();
        assertEquals("nil\n", result);
    }

    @Test
    public void variable_use_false_as_var() throws IOException {

        runFile(testDir + "/variable/use_false_as_var.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void variable_shadow_global() throws IOException {

        runFile(testDir + "/variable/shadow_global.lox");
        String result = systemOutRule.getLog();
        assertEquals("shadow\nglobal\n", result);
    }

    @Test
    public void variable_duplicate_local() throws IOException {

        runFile(testDir + "/variable/duplicate_local.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void variable_in_middle_of_block() throws IOException {

        runFile(testDir + "/variable/in_middle_of_block.lox");
        String result = systemOutRule.getLog();
        assertEquals("a\na b\na c\na b d\n", result);
    }

    @Test
    public void variable_shadow_local() throws IOException {

        runFile(testDir + "/variable/shadow_local.lox");
        String result = systemOutRule.getLog();
        assertEquals("shadow\nlocal\n", result);
    }

    @Test
    public void variable_unreached_undefined() throws IOException {

        runFile(testDir + "/variable/unreached_undefined.lox");
        String result = systemOutRule.getLog();
        assertEquals("ok\n", result);
    }

    @Test
    public void variable_collide_with_parameter() throws IOException {

        runFile(testDir + "/variable/collide_with_parameter.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void variable_use_local_in_initializer() throws IOException {

        runFile(testDir + "/variable/use_local_in_initializer.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void variable_redefine_global() throws IOException {

        runFile(testDir + "/variable/redefine_global.lox");
        String result = systemOutRule.getLog();
        assertEquals("2\n", result);
    }

    @Test
    public void variable_undefined_local() throws IOException {

        runFile(testDir + "/variable/undefined_local.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void nil_literal() throws IOException {

        runFile(testDir + "/nil/literal.lox");
        String result = systemOutRule.getLog();
        assertEquals("nil\n", result);
    }

    @Test
    public void unexpected_character() throws IOException {

        runFile(testDir + "/unexpected_character.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void if_var_in_then() throws IOException {

        runFile(testDir + "/if/var_in_then.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void if_dangling_else() throws IOException {

        runFile(testDir + "/if/dangling_else.lox");
        String result = systemOutRule.getLog();
        assertEquals("good\n", result);
    }

    @Test
    public void if_truth() throws IOException {

        runFile(testDir + "/if/truth.lox");
        String result = systemOutRule.getLog();
        assertEquals("false\nnil\ntrue\n0\nempty\n", result);
    }

    @Test
    public void if_fun_in_else() throws IOException {

        runFile(testDir + "/if/fun_in_else.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void if_class_in_else() throws IOException {

        runFile(testDir + "/if/class_in_else.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void if_else() throws IOException {

        runFile(testDir + "/if/else.lox");
        String result = systemOutRule.getLog();
        assertEquals("good\ngood\nblock\n", result);
    }

    @Test
    public void if_fun_in_then() throws IOException {

        runFile(testDir + "/if/fun_in_then.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void if_class_in_then() throws IOException {

        runFile(testDir + "/if/class_in_then.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void if_var_in_else() throws IOException {

        runFile(testDir + "/if/var_in_else.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void if_if() throws IOException {

        runFile(testDir + "/if/if.lox");
        String result = systemOutRule.getLog();
        assertEquals("good\nblock\ntrue\n", result);
    }

    @Test
    public void assignment_grouping() throws IOException {

        runFile(testDir + "/assignment/grouping.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void assignment_syntax() throws IOException {

        runFile(testDir + "/assignment/syntax.lox");
        String result = systemOutRule.getLog();
        assertEquals("var\nvar\n", result);
    }

    @Test
    public void assignment_global() throws IOException {

        runFile(testDir + "/assignment/global.lox");
        String result = systemOutRule.getLog();
        assertEquals("before\nafter\narg\narg\n", result);
    }

    @Test
    public void assignment_prefix_operator() throws IOException {

        runFile(testDir + "/assignment/prefix_operator.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void assignment_associativity() throws IOException {

        runFile(testDir + "/assignment/associativity.lox");
        String result = systemOutRule.getLog();
        assertEquals("c\nc\nc\n", result);
    }

    @Test
    public void assignment_to_this() throws IOException {

        runFile(testDir + "/assignment/to_this.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void assignment_infix_operator() throws IOException {

        runFile(testDir + "/assignment/infix_operator.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void assignment_local() throws IOException {

        runFile(testDir + "/assignment/local.lox");
        String result = systemOutRule.getLog();
        assertEquals("before\nafter\narg\narg\n", result);
    }

    @Test
    public void assignment_undefined() throws IOException {

        runFile(testDir + "/assignment/undefined.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void return_after_if() throws IOException {

        runFile(testDir + "/return/after_if.lox");
        String result = systemOutRule.getLog();
        assertEquals("ok\n", result);
    }

    @Test
    public void return_after_else() throws IOException {

        runFile(testDir + "/return/after_else.lox");
        String result = systemOutRule.getLog();
        assertEquals("ok\n", result);
    }

    @Test
    public void return_at_top_level() throws IOException {

        runFile(testDir + "/return/at_top_level.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void return_return_nil_if_no_value() throws IOException {

        runFile(testDir + "/return/return_nil_if_no_value.lox");
        String result = systemOutRule.getLog();
        assertEquals("nil\n", result);
    }

    @Test
    public void return_in_method() throws IOException {

        runFile(testDir + "/return/in_method.lox");
        String result = systemOutRule.getLog();
        assertEquals("ok\n", result);
    }

    @Test
    public void return_in_function() throws IOException {

        runFile(testDir + "/return/in_function.lox");
        String result = systemOutRule.getLog();
        assertEquals("ok\n", result);
    }

    @Test
    public void return_after_while() throws IOException {

        runFile(testDir + "/return/after_while.lox");
        String result = systemOutRule.getLog();
        assertEquals("ok\n", result);
    }

    @Test
    public void function_local_mutual_recursion() throws IOException {

        runFile(testDir + "/function/local_mutual_recursion.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void function_empty_body() throws IOException {

        runFile(testDir + "/function/empty_body.lox");
        String result = systemOutRule.getLog();
        assertEquals("nil\n", result);
    }

    @Test
    public void function_too_many_arguments() throws IOException {

        runFile(testDir + "/function/too_many_arguments.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void function_missing_comma_in_parameters() throws IOException {

        runFile(testDir + "/function/missing_comma_in_parameters.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void function_nested_call_with_arguments() throws IOException {

        runFile(testDir + "/function/nested_call_with_arguments.lox");
        String result = systemOutRule.getLog();
        assertEquals("hello world\n", result);
    }

    @Test
    public void function_body_must_be_block() throws IOException {

        runFile(testDir + "/function/body_must_be_block.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void function_missing_arguments() throws IOException {

        runFile(testDir + "/function/missing_arguments.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void function_parameters() throws IOException {

        runFile(testDir + "/function/parameters.lox");
        String result = systemOutRule.getLog();
        assertEquals("0\n1\n3\n6\n10\n15\n21\n28\n36\n", result);
    }

    @Test
    public void function_local_recursion() throws IOException {

        runFile(testDir + "/function/local_recursion.lox");
        String result = systemOutRule.getLog();
        assertEquals("21\n", result);
    }

    @Test
    public void function_recursion() throws IOException {

        runFile(testDir + "/function/recursion.lox");
        String result = systemOutRule.getLog();
        assertEquals("21\n", result);
    }

    @Test
    public void function_print() throws IOException {

        runFile(testDir + "/function/print.lox");
        String result = systemOutRule.getLog();
        assertEquals("<fn foo>\n<native fn>\n", result);
    }

    @Test
    public void function_too_many_parameters() throws IOException {

        runFile(testDir + "/function/too_many_parameters.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void function_mutual_recursion() throws IOException {

        runFile(testDir + "/function/mutual_recursion.lox");
        String result = systemOutRule.getLog();
        assertEquals("true\ntrue\n", result);
    }

    @Test
    public void function_extra_arguments() throws IOException {

        runFile(testDir + "/function/extra_arguments.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void field_set_on_nil() throws IOException {

        runFile(testDir + "/field/set_on_nil.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void field_get_on_string() throws IOException {

        runFile(testDir + "/field/get_on_string.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void field_many() throws IOException {

        runFile(testDir + "/field/many.lox");
        String result = systemOutRule.getLog();
        assertEquals("apple\napricot\navocado\nbanana\nbilberry\nblackberry\nblackcurrant\nblueberry\nboysenberry\ncantaloupe\ncherimoya\ncherry\nclementine\ncloudberry\ncoconut\ncranberry\ncurrant\ndamson\ndate\ndragonfruit\ndurian\nelderberry\nfeijoa\nfig\ngooseberry\ngrape\ngrapefruit\nguava\nhoneydew\nhuckleberry\njabuticaba\njackfruit\njambul\njujube\njuniper\nkiwifruit\nkumquat\nlemon\nlime\nlongan\nloquat\nlychee\nmandarine\nmango\nmarionberry\nmelon\nmiracle\nmulberry\nnance\nnectarine\nolive\norange\npapaya\npassionfruit\npeach\npear\npersimmon\nphysalis\npineapple\nplantain\nplum\nplumcot\npomegranate\npomelo\nquince\nraisin\nrambutan\nraspberry\nredcurrant\nsalak\nsalmonberry\nsatsuma\nstrawberry\ntamarillo\ntamarind\ntangerine\ntomato\nwatermelon\nyuzu\n", result);
    }

    @Test
    public void field_set_on_function() throws IOException {

        runFile(testDir + "/field/set_on_function.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void field_set_on_bool() throws IOException {

        runFile(testDir + "/field/set_on_bool.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void field_method() throws IOException {

        runFile(testDir + "/field/method.lox");
        String result = systemOutRule.getLog();
        assertEquals("got method\narg\n", result);
    }

    @Test
    public void field_call_nonfunction_field() throws IOException {

        runFile(testDir + "/field/call_nonfunction_field.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void field_get_on_nil() throws IOException {

        runFile(testDir + "/field/get_on_nil.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void field_set_on_class() throws IOException {

        runFile(testDir + "/field/set_on_class.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void field_set_on_string() throws IOException {

        runFile(testDir + "/field/set_on_string.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void field_on_instance() throws IOException {

        runFile(testDir + "/field/on_instance.lox");
        String result = systemOutRule.getLog();
        assertEquals("bar value\nbaz value\nbar value\nbaz value\n", result);
    }

    @Test
    public void field_get_on_function() throws IOException {

        runFile(testDir + "/field/get_on_function.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void field_call_function_field() throws IOException {

        runFile(testDir + "/field/call_function_field.lox");
        String result = systemOutRule.getLog();
        assertEquals("bar\n1\n2\n", result);
    }

    @Test
    public void field_set_evaluation_order() throws IOException {

        runFile(testDir + "/field/set_evaluation_order.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void field_method_binds_this() throws IOException {

        runFile(testDir + "/field/method_binds_this.lox");
        String result = systemOutRule.getLog();
        assertEquals("foo1\n1\n", result);
    }

    @Test
    public void field_set_on_num() throws IOException {

        runFile(testDir + "/field/set_on_num.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void field_get_on_class() throws IOException {

        runFile(testDir + "/field/get_on_class.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void field_get_and_set_method() throws IOException {

        runFile(testDir + "/field/get_and_set_method.lox");
        String result = systemOutRule.getLog();
        assertEquals("other\n1\nmethod\n2\n", result);
    }

    @Test
    public void field_get_on_bool() throws IOException {

        runFile(testDir + "/field/get_on_bool.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void field_get_on_num() throws IOException {

        runFile(testDir + "/field/get_on_num.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void field_undefined() throws IOException {

        runFile(testDir + "/field/undefined.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void variadic_function() throws IOException {

        runFile(testDir + "/variadic_function.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void number_decimal_point_at_eof() throws IOException {

        runFile(testDir + "/number/decimal_point_at_eof.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void number_nan_equality() throws IOException {

        runFile(testDir + "/number/nan_equality.lox");
        String result = systemOutRule.getLog();
        assertEquals("false\ntrue\ntrue\nfalse\n", result);
    }

    @Test
    public void number_literals() throws IOException {

        runFile(testDir + "/number/literals.lox");
        String result = systemOutRule.getLog();
        assertEquals("123\n987654\n0\n-0\n123.456\n-0.001\n", result);
    }

    @Test
    public void number_leading_dot() throws IOException {

        runFile(testDir + "/number/leading_dot.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void number_trailing_dot() throws IOException {

        runFile(testDir + "/number/trailing_dot.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void call_nil() throws IOException {

        runFile(testDir + "/call/nil.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void call_bool() throws IOException {

        runFile(testDir + "/call/bool.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void call_num() throws IOException {

        runFile(testDir + "/call/num.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void call_object() throws IOException {

        runFile(testDir + "/call/object.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void call_string() throws IOException {

        runFile(testDir + "/call/string.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void logical_operator_and() throws IOException {

        runFile(testDir + "/logical_operator/and.lox");
        String result = systemOutRule.getLog();
        assertEquals("false\n1\nfalse\ntrue\n3\ntrue\nfalse\n", result);
    }

    @Test
    public void logical_operator_or() throws IOException {

        runFile(testDir + "/logical_operator/or.lox");
        String result = systemOutRule.getLog();
        assertEquals("1\n1\ntrue\nfalse\nfalse\nfalse\ntrue\n", result);
    }

    @Test
    public void logical_operator_and_truth() throws IOException {

        runFile(testDir + "/logical_operator/and_truth.lox");
        String result = systemOutRule.getLog();
        assertEquals("false\nnil\nok\nok\nok\n", result);
    }

    @Test
    public void logical_operator_or_truth() throws IOException {

        runFile(testDir + "/logical_operator/or_truth.lox");
        String result = systemOutRule.getLog();
        assertEquals("ok\nok\ntrue\n0\ns\n", result);
    }

    @Test
    public void inheritance_inherit_from_nil() throws IOException {

        runFile(testDir + "/inheritance/inherit_from_nil.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void inheritance_inherit_from_function() throws IOException {

        runFile(testDir + "/inheritance/inherit_from_function.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void inheritance_parenthesized_superclass() throws IOException {

        runFile(testDir + "/inheritance/parenthesized_superclass.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void inheritance_set_fields_from_base_class() throws IOException {

        runFile(testDir + "/inheritance/set_fields_from_base_class.lox");
        String result = systemOutRule.getLog();
        assertEquals("foo 1\nfoo 2\nbar 1\nbar 2\nbar 1\nbar 2\n", result);
    }

    @Test
    public void inheritance_inherit_from_number() throws IOException {

        runFile(testDir + "/inheritance/inherit_from_number.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void inheritance_inherit_methods() throws IOException {

        runFile(testDir + "/inheritance/inherit_methods.lox");
        String result = systemOutRule.getLog();
        assertEquals("foo\nbar\nbar\n", result);
    }

    @Test
    public void inheritance_constructor() throws IOException {

        runFile(testDir + "/inheritance/constructor.lox");
        String result = systemOutRule.getLog();
        assertEquals("value\n", result);
    }

    @Test
    public void super_no_superclass_method() throws IOException {

        runFile(testDir + "/super/no_superclass_method.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void super_call_same_method() throws IOException {

        runFile(testDir + "/super/call_same_method.lox");
        String result = systemOutRule.getLog();
        assertEquals("Derived.foo()\nBase.foo()\n", result);
    }

    @Test
    public void super_no_superclass_call() throws IOException {

        runFile(testDir + "/super/no_superclass_call.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void super_no_superclass_bind() throws IOException {

        runFile(testDir + "/super/no_superclass_bind.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void super_parenthesized() throws IOException {

        runFile(testDir + "/super/parenthesized.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void super_this_in_superclass_method() throws IOException {

        runFile(testDir + "/super/this_in_superclass_method.lox");
        String result = systemOutRule.getLog();
        assertEquals("a\nb\n", result);
    }

    @Test
    public void super_closure() throws IOException {

        runFile(testDir + "/super/closure.lox");
        String result = systemOutRule.getLog();
        assertEquals("Base\n", result);
    }

    @Test
    public void super_super_in_top_level_function() throws IOException {

        runFile(testDir + "/super/super_in_top_level_function.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void super_call_other_method() throws IOException {

        runFile(testDir + "/super/call_other_method.lox");
        String result = systemOutRule.getLog();
        assertEquals("Derived.bar()\nBase.foo()\n", result);
    }

    @Test
    public void super_missing_arguments() throws IOException {

        runFile(testDir + "/super/missing_arguments.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void super_super_in_closure_in_inherited_method() throws IOException {

        runFile(testDir + "/super/super_in_closure_in_inherited_method.lox");
        String result = systemOutRule.getLog();
        assertEquals("A\n", result);
    }

    @Test
    public void super_super_in_inherited_method() throws IOException {

        runFile(testDir + "/super/super_in_inherited_method.lox");
        String result = systemOutRule.getLog();
        assertEquals("A\n", result);
    }

    @Test
    public void super_super_without_dot() throws IOException {

        runFile(testDir + "/super/super_without_dot.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void super_indirectly_inherited() throws IOException {

        runFile(testDir + "/super/indirectly_inherited.lox");
        String result = systemOutRule.getLog();
        assertEquals("C.foo()\nA.foo()\n", result);
    }

    @Test
    public void super_super_at_top_level() throws IOException {

        runFile(testDir + "/super/super_at_top_level.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void super_super_without_name() throws IOException {

        runFile(testDir + "/super/super_without_name.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void super_extra_arguments() throws IOException {

        runFile(testDir + "/super/extra_arguments.lox");
        String result = systemOutRule.getLog();
        assertEquals("Derived.foo()\n", result);
    }

    @Test
    public void super_bound_method() throws IOException {

        runFile(testDir + "/super/bound_method.lox");
        String result = systemOutRule.getLog();
        assertEquals("A.method(arg)\n", result);
    }

    @Test
    public void super_constructor() throws IOException {

        runFile(testDir + "/super/constructor.lox");
        String result = systemOutRule.getLog();
        assertEquals("Derived.init()\nBase.init(a, b)\n", result);
    }

    @Test
    public void super_reassign_superclass() throws IOException {

        runFile(testDir + "/super/reassign_superclass.lox");
        String result = systemOutRule.getLog();
        assertEquals("Base.method()\nBase.method()\n", result);
    }

    @Test
    public void array_spread() throws IOException {

        runFile(testDir + "/array_spread.lox");
        String result = systemOutRule.getLog();
        assertEquals("1\n2\n3\n4\n5\n6\n", result);
    }

    @Test
    public void bool_equality() throws IOException {

        runFile(testDir + "/bool/equality.lox");
        String result = systemOutRule.getLog();
        assertEquals("true\nfalse\nfalse\ntrue\nfalse\nfalse\nfalse\nfalse\nfalse\nfalse\ntrue\ntrue\nfalse\ntrue\ntrue\ntrue\ntrue\ntrue\n", result);
    }

    @Test
    public void bool_not() throws IOException {

        runFile(testDir + "/bool/not.lox");
        String result = systemOutRule.getLog();
        assertEquals("false\ntrue\ntrue\n", result);
    }

    @Test
    public void for_return_closure() throws IOException {

        runFile(testDir + "/for/return_closure.lox");
        String result = systemOutRule.getLog();
        assertEquals("i\n", result);
    }

    @Test
    public void for_scope() throws IOException {

        runFile(testDir + "/for/scope.lox");
        String result = systemOutRule.getLog();
        assertEquals("0\n-1\nafter\n0\n", result);
    }

    @Test
    public void for_var_in_body() throws IOException {

        runFile(testDir + "/for/var_in_body.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void for_syntax() throws IOException {

        runFile(testDir + "/for/syntax.lox");
        String result = systemOutRule.getLog();
        assertEquals("1\n2\n3\n0\n1\n2\ndone\n0\n1\n0\n1\n2\n0\n1\n", result);
    }

    @Test
    public void for_return_inside() throws IOException {

        runFile(testDir + "/for/return_inside.lox");
        String result = systemOutRule.getLog();
        assertEquals("i\n", result);
    }

    @Test
    public void for_statement_initializer() throws IOException {

        runFile(testDir + "/for/statement_initializer.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void for_statement_increment() throws IOException {

        runFile(testDir + "/for/statement_increment.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void for_statement_condition() throws IOException {

        runFile(testDir + "/for/statement_condition.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void for_closure_in_body() throws IOException {

        runFile(testDir + "/for/closure_in_body.lox");
        String result = systemOutRule.getLog();
        assertEquals("4\n1\n4\n2\n4\n3\n", result);
    }

    @Test
    public void for_class_in_body() throws IOException {

        runFile(testDir + "/for/class_in_body.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void for_fun_in_body() throws IOException {

        runFile(testDir + "/for/fun_in_body.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void class_empty() throws IOException {

        runFile(testDir + "/class/empty.lox");
        String result = systemOutRule.getLog();
        assertEquals("Foo\n", result);
    }

    @Test
    public void class_local_inherit_self() throws IOException {

        runFile(testDir + "/class/local_inherit_self.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void class_local_inherit_other() throws IOException {

        runFile(testDir + "/class/local_inherit_other.lox");
        String result = systemOutRule.getLog();
        assertEquals("B\n", result);
    }

    @Test
    public void class_inherited_method() throws IOException {

        runFile(testDir + "/class/inherited_method.lox");
        String result = systemOutRule.getLog();
        assertEquals("in foo\nin bar\nin baz\n", result);
    }

    @Test
    public void class_reference_self() throws IOException {

        runFile(testDir + "/class/reference_self.lox");
        String result = systemOutRule.getLog();
        assertEquals("Foo\n", result);
    }

    @Test
    public void class_inherit_self() throws IOException {

        runFile(testDir + "/class/inherit_self.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void class_local_reference_self() throws IOException {

        runFile(testDir + "/class/local_reference_self.lox");
        String result = systemOutRule.getLog();
        assertEquals("Foo\n", result);
    }

    @Test
    public void this_this_in_method() throws IOException {

        runFile(testDir + "/this/this_in_method.lox");
        String result = systemOutRule.getLog();
        assertEquals("baz\n", result);
    }

    @Test
    public void this_this_at_top_level() throws IOException {

        runFile(testDir + "/this/this_at_top_level.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void this_closure() throws IOException {

        runFile(testDir + "/this/closure.lox");
        String result = systemOutRule.getLog();
        assertEquals("Foo\n", result);
    }

    @Test
    public void this_this_in_top_level_function() throws IOException {

        runFile(testDir + "/this/this_in_top_level_function.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void this_nested_closure() throws IOException {

        runFile(testDir + "/this/nested_closure.lox");
        String result = systemOutRule.getLog();
        assertEquals("Foo\n", result);
    }

    @Test
    public void this_nested_class() throws IOException {

        runFile(testDir + "/this/nested_class.lox");
        String result = systemOutRule.getLog();
        assertEquals("Outer instance\nOuter instance\nInner instance\n", result);
    }

    @Test
    public void string_error_after_multiline() throws IOException {

        runFile(testDir + "/string/error_after_multiline.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void string_literals() throws IOException {

        runFile(testDir + "/string/literals.lox");
        String result = systemOutRule.getLog();
        assertEquals("()\na string\nA~¶Þॐஃ\n", result);
    }

    @Test
    public void string_multiline() throws IOException {

        runFile(testDir + "/string/multiline.lox");
        String result = systemOutRule.getLog();
        assertEquals("1\n2\n3\n", result);
    }

    @Test
    public void string_unterminated() throws IOException {

        runFile(testDir + "/string/unterminated.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void precedence() throws IOException {

        runFile(testDir + "/precedence.lox");
        String result = systemOutRule.getLog();
        assertEquals("14\n8\n4\n0\ntrue\ntrue\ntrue\ntrue\n0\n0\n0\n0\n4\n", result);
    }

    @Test
    public void regression_40() throws IOException {

        runFile(testDir + "/regression/40.lox");
        String result = systemOutRule.getLog();
        assertEquals("false\n", result);
    }

    @Test
    public void regression_394() throws IOException {

        runFile(testDir + "/regression/394.lox");
        String result = systemOutRule.getLog();
        assertEquals("B\n", result);
    }

    @Test
    public void while_return_closure() throws IOException {

        runFile(testDir + "/while/return_closure.lox");
        String result = systemOutRule.getLog();
        assertEquals("i\n", result);
    }

    @Test
    public void while_var_in_body() throws IOException {

        runFile(testDir + "/while/var_in_body.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void while_syntax() throws IOException {

        runFile(testDir + "/while/syntax.lox");
        String result = systemOutRule.getLog();
        assertEquals("1\n2\n3\n0\n1\n2\n", result);
    }

    @Test
    public void while_return_inside() throws IOException {

        runFile(testDir + "/while/return_inside.lox");
        String result = systemOutRule.getLog();
        assertEquals("i\n", result);
    }

    @Test
    public void while_closure_in_body() throws IOException {

        runFile(testDir + "/while/closure_in_body.lox");
        String result = systemOutRule.getLog();
        assertEquals("1\n2\n3\n", result);
    }

    @Test
    public void while_class_in_body() throws IOException {

        runFile(testDir + "/while/class_in_body.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void while_fun_in_body() throws IOException {

        runFile(testDir + "/while/fun_in_body.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void method_empty_block() throws IOException {

        runFile(testDir + "/method/empty_block.lox");
        String result = systemOutRule.getLog();
        assertEquals("nil\n", result);
    }

    @Test
    public void method_arity() throws IOException {

        runFile(testDir + "/method/arity.lox");
        String result = systemOutRule.getLog();
        assertEquals("no args\n1\n3\n6\n10\n15\n21\n28\n36\n", result);
    }

    @Test
    public void method_refer_to_name() throws IOException {

        runFile(testDir + "/method/refer_to_name.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void method_too_many_arguments() throws IOException {

        runFile(testDir + "/method/too_many_arguments.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void method_print_bound_method() throws IOException {

        runFile(testDir + "/method/print_bound_method.lox");
        String result = systemOutRule.getLog();
        assertEquals("<fn method>\n", result);
    }

    @Test
    public void method_missing_arguments() throws IOException {

        runFile(testDir + "/method/missing_arguments.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void method_not_found() throws IOException {

        runFile(testDir + "/method/not_found.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void method_too_many_parameters() throws IOException {

        runFile(testDir + "/method/too_many_parameters.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void method_extra_arguments() throws IOException {

        runFile(testDir + "/method/extra_arguments.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_add_num_nil() throws IOException {

        runFile(testDir + "/operator/add_num_nil.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_equals_method() throws IOException {

        runFile(testDir + "/operator/equals_method.lox");
        String result = systemOutRule.getLog();
        assertEquals("true\nfalse\n", result);
    }

    @Test
    public void operator_equals_class() throws IOException {

        runFile(testDir + "/operator/equals_class.lox");
        String result = systemOutRule.getLog();
        assertEquals("true\nfalse\nfalse\ntrue\nfalse\nfalse\nfalse\nfalse\n", result);
    }

    @Test
    public void operator_subtract_num_nonnum() throws IOException {

        runFile(testDir + "/operator/subtract_num_nonnum.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_multiply() throws IOException {

        runFile(testDir + "/operator/multiply.lox");
        String result = systemOutRule.getLog();
        assertEquals("15\n3.702\n", result);
    }

    @Test
    public void operator_negate() throws IOException {

        runFile(testDir + "/operator/negate.lox");
        String result = systemOutRule.getLog();
        assertEquals("-3\n", result);
    }

    @Test
    public void operator_divide_nonnum_num() throws IOException {

        runFile(testDir + "/operator/divide_nonnum_num.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_comparison() throws IOException {

        runFile(testDir + "/operator/comparison.lox");
        String result = systemOutRule.getLog();
        assertEquals("true\nfalse\nfalse\ntrue\ntrue\nfalse\nfalse\nfalse\ntrue\nfalse\ntrue\ntrue\nfalse\nfalse\nfalse\nfalse\ntrue\ntrue\ntrue\ntrue\n", result);
    }

    @Test
    public void operator_greater_num_nonnum() throws IOException {

        runFile(testDir + "/operator/greater_num_nonnum.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_less_or_equal_nonnum_num() throws IOException {

        runFile(testDir + "/operator/less_or_equal_nonnum_num.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_multiply_nonnum_num() throws IOException {

        runFile(testDir + "/operator/multiply_nonnum_num.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_not_equals() throws IOException {

        runFile(testDir + "/operator/not_equals.lox");
        String result = systemOutRule.getLog();
        assertEquals("false\nfalse\ntrue\nfalse\ntrue\nfalse\ntrue\ntrue\ntrue\ntrue\n", result);
    }

    @Test
    public void operator_add_bool_num() throws IOException {

        runFile(testDir + "/operator/add_bool_num.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_negate_nonnum() throws IOException {

        runFile(testDir + "/operator/negate_nonnum.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_add() throws IOException {

        runFile(testDir + "/operator/add.lox");
        String result = systemOutRule.getLog();
        assertEquals("579\nstring\n", result);
    }

    @Test
    public void operator_greater_or_equal_nonnum_num() throws IOException {

        runFile(testDir + "/operator/greater_or_equal_nonnum_num.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_equals() throws IOException {

        runFile(testDir + "/operator/equals.lox");
        String result = systemOutRule.getLog();
        assertEquals("true\ntrue\nfalse\ntrue\nfalse\ntrue\nfalse\nfalse\nfalse\nfalse\n", result);
    }

    @Test
    public void operator_less_nonnum_num() throws IOException {

        runFile(testDir + "/operator/less_nonnum_num.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_add_bool_string() throws IOException {

        runFile(testDir + "/operator/add_bool_string.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_divide() throws IOException {

        runFile(testDir + "/operator/divide.lox");
        String result = systemOutRule.getLog();
        assertEquals("4\n1\n", result);
    }

    @Test
    public void operator_add_string_nil() throws IOException {

        runFile(testDir + "/operator/add_string_nil.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_add_bool_nil() throws IOException {

        runFile(testDir + "/operator/add_bool_nil.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_divide_num_nonnum() throws IOException {

        runFile(testDir + "/operator/divide_num_nonnum.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_multiply_num_nonnum() throws IOException {

        runFile(testDir + "/operator/multiply_num_nonnum.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_less_or_equal_num_nonnum() throws IOException {

        runFile(testDir + "/operator/less_or_equal_num_nonnum.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_greater_nonnum_num() throws IOException {

        runFile(testDir + "/operator/greater_nonnum_num.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_not() throws IOException {

        runFile(testDir + "/operator/not.lox");
        String result = systemOutRule.getLog();
        assertEquals("false\ntrue\ntrue\nfalse\nfalse\ntrue\nfalse\nfalse\n", result);
    }

    @Test
    public void operator_add_nil_nil() throws IOException {

        runFile(testDir + "/operator/add_nil_nil.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_subtract() throws IOException {

        runFile(testDir + "/operator/subtract.lox");
        String result = systemOutRule.getLog();
        assertEquals("1\n0\n", result);
    }

    @Test
    public void operator_subtract_nonnum_num() throws IOException {

        runFile(testDir + "/operator/subtract_nonnum_num.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_not_class() throws IOException {

        runFile(testDir + "/operator/not_class.lox");
        String result = systemOutRule.getLog();
        assertEquals("false\nfalse\n", result);
    }

    @Test
    public void operator_greater_or_equal_num_nonnum() throws IOException {

        runFile(testDir + "/operator/greater_or_equal_num_nonnum.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void operator_less_num_nonnum() throws IOException {

        runFile(testDir + "/operator/less_num_nonnum.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void constructor_call_init_explicitly() throws IOException {

        runFile(testDir + "/constructor/call_init_explicitly.lox");
        String result = systemOutRule.getLog();
        assertEquals("Foo.init(one)\nFoo.init(two)\nFoo instance\ninit\n", result);
    }

    @Test
    public void constructor_return_value() throws IOException {

        runFile(testDir + "/constructor/return_value.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void constructor_init_not_method() throws IOException {

        runFile(testDir + "/constructor/init_not_method.lox");
        String result = systemOutRule.getLog();
        assertEquals("not initializer\n", result);
    }

    @Test
    public void constructor_missing_arguments() throws IOException {

        runFile(testDir + "/constructor/missing_arguments.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void constructor_default() throws IOException {

        runFile(testDir + "/constructor/default.lox");
        String result = systemOutRule.getLog();
        assertEquals("Foo instance\n", result);
    }

    @Test
    public void constructor_arguments() throws IOException {

        runFile(testDir + "/constructor/arguments.lox");
        String result = systemOutRule.getLog();
        assertEquals("init\n1\n2\n", result);
    }

    @Test
    public void constructor_default_arguments() throws IOException {

        runFile(testDir + "/constructor/default_arguments.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void constructor_call_init_early_return() throws IOException {

        runFile(testDir + "/constructor/call_init_early_return.lox");
        String result = systemOutRule.getLog();
        assertEquals("init\ninit\nFoo instance\n", result);
    }

    @Test
    public void constructor_extra_arguments() throws IOException {

        runFile(testDir + "/constructor/extra_arguments.lox");
        String result = systemOutRule.getLog();
        assertEquals("", result);
    }

    @Test
    public void constructor_return_in_nested_function() throws IOException {

        runFile(testDir + "/constructor/return_in_nested_function.lox");
        String result = systemOutRule.getLog();
        assertEquals("bar\nFoo instance\n", result);
    }

    @Test
    public void constructor_early_return() throws IOException {

        runFile(testDir + "/constructor/early_return.lox");
        String result = systemOutRule.getLog();
        assertEquals("init\nFoo instance\n", result);
    }

    @Test
    public void block_empty() throws IOException {

        runFile(testDir + "/block/empty.lox");
        String result = systemOutRule.getLog();
        assertEquals("ok\n", result);
    }

    @Test
    public void block_scope() throws IOException {

        runFile(testDir + "/block/scope.lox");
        String result = systemOutRule.getLog();
        assertEquals("inner\nouter\n", result);
    }

}
