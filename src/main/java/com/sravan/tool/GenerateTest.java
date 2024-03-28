package com.sravan.tool;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GenerateTest {
    private static final String testDir = "src/test/java/com/sravan/lox/testcases";
    private static final String outputFile = "src/test/java/com/sravan/lox/LoxTest.java";

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
        writer.println("package com.sravan.lox;");
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
        writer.println("public class LoxTest {");
        writer.println("    private static final String testDir = \"src/test/java/com/sravan/lox/testcases\";");
        writer.println();
        writer.println("    private static final void runFile(String path) throws IOException {");
        writer.println("        Compiler compiler = new Compiler();");
        writer.println("        byte[] bytes = Files.readAllBytes(Paths.get(path));");
        writer.println("        compiler.run(new String(bytes, Charset.defaultCharset()));");
        writer.println("    }");
        writer.println();
        writer.println("    @Rule");
        writer.println("    public final SystemOutRule systemOutRule = new SystemOutRule().enableLog();");
        writer.println();
        writer.println("    @Rule");
        writer.println("    public final SystemErrRule systemErrRule = new SystemErrRule().enableLog();");
        writer.println();
        List<String> testFiles = getTestFiles();
        for (String testFile : testFiles) {
            String folderName = new File(testFile).getParentFile().getName();
            String fileName = new File(testFile).getName();
            String testName = (folderName.equals("testcases") ? "" : (folderName + "_")) + fileName.replace(".lox", "");
            String fileNameForTest = folderName.equals("testcases") ? fileName : folderName + "/" + fileName;
            String expectedOutput = getExpectedOutput(testFile);
            writer.println("    @Test");
            writer.println("    public void " + testName + "() throws IOException {");
            writer.println();
            writer.println("        runFile(testDir + \"/" + fileNameForTest + "\");");
            writer.println("        String result = systemOutRule.getLog();");
            writer.println("        assertEquals(\"" + (expectedOutput) + "\", result);");
            writer.println("    }");
            writer.println();
        }
        writer.println("}");
        writer.close();
    }

    private static String getExpectedOutput(String testFile) throws IOException {

        File file = new File(testFile);
        String content = new String(Files.readAllBytes(file.toPath()));
        Pattern pattern = Pattern.compile("// expect: (.*)\n");
        Matcher matcher = pattern.matcher(content);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            sb.append(matcher.group(1)).append("\\n");
        }
        return sb.toString();
    }

    private static List<String> getTestFiles() throws IOException {
        List<String> testFiles = Files.walk(Paths.get(testDir)).filter(Files::isRegularFile)
                .map(x -> x.toString()).filter(f -> f.endsWith(".lox")).toList();
        return testFiles;
    }
}
