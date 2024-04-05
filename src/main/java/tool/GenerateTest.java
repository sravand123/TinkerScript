package tool;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateTest {
    private static final String testDir = "src/test/java/tinkerscript/testcases";
    private static final String outputFile = "src/test/java/tinkerscript/TestCaseRunner.java";

    public static void main(String[] args) {
        System.out.println("Generating test file...");
        try {
            generateTestFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Test file generated successfully.");
    }

    private static void generateTestFile() throws Exception {
        PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
        writer.println("package tinkerscript;");
        writer.println();
        writer.println("import static org.junit.Assert.assertEquals;");
        writer.println();
        writer.println("import java.io.IOException;");
        writer.println("import java.nio.charset.Charset;");
        writer.println("import java.nio.file.Files;");
        writer.println("import java.nio.file.Paths;");
        writer.println();
        writer.println("import org.junit.Rule;");
        writer.println("import org.junit.Test;");
        writer.println("import org.junit.contrib.java.lang.system.SystemErrRule;");
        writer.println("import org.junit.contrib.java.lang.system.SystemOutRule;");
        writer.println();
        writer.println("public class TestCaseRunner {");
        writer.println("    private static final String testDir = \"src/test/java/tinkerscript/testcases\";");
        writer.println();
        writer.println();
        writer.println("    @Rule");
        writer.println("    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();");
        writer.println();
        writer.println("    @Rule");
        writer.println("    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();");
        writer.println();
        writer.println("    private final String getFinalConsoleOutput(String result, String error) {");
        writer.println("        if (error.startsWith(\"runtime error:\")) {");
        writer.println("            error = error.replaceAll(\" \\\\[line \\\\d+\\\\]\", \"\");");
        writer.println("        }");
        writer.println("        if (result.isEmpty() && error.isEmpty())");
        writer.println("            return \"\";");
        writer.println("        if (error.isEmpty())");
        writer.println("            return result;");
        writer.println("        if (result.isEmpty())");
        writer.println("            return error;");
        writer.println("        return result + error;");
        writer.println("    }");
        writer.println(
                "    private final String testFile(String path) throws IOException {");
        writer.println("        Compiler compiler = new Compiler();");
        writer.println("        byte[] bytes = Files.readAllBytes(Paths.get(path));");
        writer.println("        compiler.run(new String(bytes, Charset.defaultCharset()));");
        writer.println("        String result = systemOutRule.getLog();");
        writer.println("        String error = systemErrRule.getLog();");
        writer.println("        String output = getFinalConsoleOutput(result, error);");
        writer.println("        return output;");
        writer.println("    }");
        writer.println();
        List<String> testFiles = getTestFiles();
        for (String testFile : testFiles) {
            String folderName = new File(testFile).getParentFile().getName();
            String fileName = new File(testFile).getName();
            String testName = (folderName.equals("testcases") ? "" : (folderName + "_")) + fileName.replace(".tis", "");
            String fileNameForTest = folderName.equals("testcases") ? fileName : folderName + "/" + fileName;
            String expectedOutput = getExpectedOutput(testFile);
            writer.println("    @Test");
            writer.println("    public void " + testName + "() throws IOException {");
            writer.println("        String output= testFile(testDir + \"/" + fileNameForTest + "\");");
            writer.println("        assertEquals(" + "\"" + expectedOutput + "\"" + ", output);");
            writer.println("    }");
            writer.println();
        }
        writer.println("}");
        writer.close();
    }

    private static String getExpectedOutput(String testFile) throws IOException {

        File file = new File(testFile);
        String content = new String(Files.readAllBytes(file.toPath()));

        String expectedResult = getExpectedOutFromPattern(content, "// expect: (.*)");

        String expectedError = getExpectedOutFromPattern(content, "// (\\[line .*\\] Error.*)");
        String expectedRuntimeError = getExpectedOutFromPattern(content, "// expect (runtime error: .*)");
        if (expectedError.length() > 0 && expectedRuntimeError.length() > 0) {
            System.err.println(
                    "Error: A program cannot have both compile time error and runtime error at the same time.");
            System.exit(1);
        }
        if (expectedError.length() > 0 && expectedResult.length() > 0) {
            System.err.println(
                    "Error: A program cannot have both compile time error and expected output at the same time.");
            System.exit(1);
        }
        String expectedOutput = "";
        if (expectedError.length() > 0) {
            expectedOutput = expectedError;
        } else {
            expectedOutput = expectedResult + expectedRuntimeError;
        }
        return expectedOutput;
    }

    private static String getExpectedOutFromPattern(String content, String regex) {
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            sb.append(matcher.group(1)).append("\\n");
        }
        return sb.toString();
    }

    private static List<String> getTestFiles() throws IOException {
        List<String> testFiles = Files.walk(Paths.get(testDir)).filter(Files::isRegularFile)
                .map(x -> x.toString()).filter(f -> f.endsWith(".tis")).toList();
        return testFiles;
    }
}
