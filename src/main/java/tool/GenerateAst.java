package tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        String outputDir = "src/main/java/tinkerscript";
        defineAst(outputDir, "Expr", Arrays.asList(
                "Binary  : Expr left, Token operator, Expr right",
                "Grouping : Expr expression",
                "Literal  : Object value",
                "Logical  : Expr left, Token operator, Expr right",
                "Unary    : Token operator, Expr right",
                "PostFix : Token operator, Expr left",
                "PreFix : Token operator, Expr right",
                "Call     : Expr callee , Token paren, List<Expr> arguments",
                "Variable : Token name",
                "Assign : Token name, Expr value",
                "Get : Expr object, Token name",
                "Set : Expr object, Token name, Expr value",
                "This : Token keyword",
                "Super : Token keyword , Token method",
                "Ternary : Expr condition , Expr left, Expr right",
                "Array : List<Expr> elements",
                "KeyAccess : Expr object, Expr key, Token rightSqParen",
                "KeySet : Expr object, Expr key, Expr value , Token equals",
                "Slice : Expr array, Expr start, Expr end,  Token rightSqParen",
                "Dictionary : List<Expr> keys, List<Expr> values",
                "Spread : Token operator, Expr right",
                "Function : Stmt.Function function",
                "Lambda : List<Token> params, Token spread , Expr body"));

        defineAst(outputDir, "Stmt", Arrays.asList(
                "Expression : Expr expression",
                "Var : Token name, Expr initializer",
                "Block : List<Stmt> statements",
                "If : Expr condition , Stmt thenStatement , Stmt elseStatement",
                "While : Expr condition,  Stmt body",
                "For : Stmt initializer, Expr condition, Expr increment, Stmt body",
                "Function : Token name, List<Token> params, Token spread , List<Stmt> body, Token staticToken, Boolean isGetter",
                "Return : Token keyword, Expr value",
                "Class : Token name, List<Stmt.Function> methods, Expr.Variable superClass",
                "TryCatch : List<Stmt> tryBlock, List<Stmt> catchBlock, Token exception",
                "Throw : Token keyword, Expr value",
                "Break : Token keyword",
                "Continue : Token keyword",
                "Case : Expr value, List<Stmt> body",
                "Switch : Expr value, List<Stmt.Case> cases"));

    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writer.println("package tinkerscript;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("abstract class " + baseName + " {");
        writer.println();
        defineVisitor(writer, baseName, types);
        writer.println();
        for (String type : types) {
            String className = type.split(":")[0].trim();
            String fieldList = type.split(":")[1].trim();
            defineType(writer, baseName, className, fieldList);
            writer.println();
        }

        writer.println("    abstract <R> R accept(Visitor<R> visitor);");

        writer.println("}");
        writer.close();
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println("    static class " + className + " extends " + baseName + " {");
        writer.println("        " + className + "(" + fieldList + ") {");

        String[] fields = fieldList.split(", ");
        for (String field : fields) {
            field = field.trim();
            String name = field.split(" ")[1];
            writer.println("            this." + name + " = " + name + ";");
        }

        writer.println("        }");
        writer.println();

        for (String field : fields) {
            field = field.trim();
            writer.println("        final " + field + ";");
        }

        writer.println();
        writer.println("        <R> R accept(Visitor<R> visitor) {");
        writer.println("            return visitor.visit" + className + baseName + "(this);");
        writer.println("        }");
        writer.println("    }");

    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("    interface Visitor<R> {");
        for (String type : types) {
            String typeName = type.split(" ")[0].trim();
            writer.println(
                    "        R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
            writer.println();
        }
        writer.println("    }");

    }
}
