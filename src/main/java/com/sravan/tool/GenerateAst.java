package com.sravan.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Usage generate_ast outputDir");
            System.exit(64);
        }
        String outputDir = args[0];
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
                "ArrayAccess : Expr array, Expr index, Token rightSqParen",
                "ArraySet : Expr array, Expr index, Expr value , Token equals",
                "ObjectLiteral : List<Token> keys, List<Expr> values",
                "Spread : Token operator, Expr right"));

        defineAst(outputDir, "Stmt", Arrays.asList(
                "Print : Expr expression",
                "Expression : Expr expression",
                "Var : Token name, Expr initializer",
                "Block : List<Stmt> statements",
                "If : Expr condition , Stmt thenStatement , Stmt elseStatement",
                "While : Expr condition,  Stmt body",
                "Function : Token name, List<Token> params, List<Stmt> body",
                "Return : Token keyword, Expr value",
                "Class : Token name, List<Stmt.Function> methods, Expr.Variable superClass"));

    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");
        writer.println("package com.sravan.lox;");
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
