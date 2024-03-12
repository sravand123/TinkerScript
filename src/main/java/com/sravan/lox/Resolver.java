package com.sravan.lox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.sravan.lox.Expr.Assign;
import com.sravan.lox.Expr.Binary;
import com.sravan.lox.Expr.Call;
import com.sravan.lox.Expr.Grouping;
import com.sravan.lox.Expr.Literal;
import com.sravan.lox.Expr.Logical;
import com.sravan.lox.Expr.Unary;
import com.sravan.lox.Expr.Variable;
import com.sravan.lox.Stmt.Block;
import com.sravan.lox.Stmt.Expression;
import com.sravan.lox.Stmt.Function;
import com.sravan.lox.Stmt.If;
import com.sravan.lox.Stmt.Print;
import com.sravan.lox.Stmt.Return;
import com.sravan.lox.Stmt.Var;
import com.sravan.lox.Stmt.While;

public class Resolver implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

    private final Stack<Map<String, Boolean>> scopes = new Stack<>();
    private final Interpreter interpreter;

    private enum FunctionType {
        NONE,
        FUNCTION
    };

    private FunctionType currentFunction = FunctionType.NONE;

    Resolver(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    void resolve(List<Stmt> statements) {
        for (Stmt stmt : statements) {
            resolve(stmt);
        }
    }

    void resolve(Stmt statement) {
        statement.accept(this);
    }

    void resolve(Expr expression) {
        expression.accept(this);
    }

    void beginScope() {
        scopes.push(new HashMap<String, Boolean>());
    }

    void endScope() {
        scopes.pop();
    }

    void declare(Token name) {
        if (scopes.isEmpty())
            return;
        if (scopes.peek().containsKey(name.lexeme)) {
            Lox.error(name, "Already a variable with this name in the scope.");
        }
        scopes.peek().put(name.lexeme, false);
    }

    void define(Token name) {
        if (scopes.isEmpty())
            return;
        scopes.peek().put(name.lexeme, true);
    }

    void resolveLocal(Expr expr, Token name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name.lexeme)) {
                interpreter.resolve(expr, scopes.size() - 1 - i);
            }
        }
    }

    void resolveFunction(Stmt.Function function, FunctionType functionType) {
        FunctionType enclosingFunction = currentFunction;
        currentFunction = functionType;
        beginScope();

        for (Token param : function.params) {
            declare(param);
            define(param);
        }
        resolve(function.body);
        endScope();
        currentFunction = enclosingFunction;
    }

    @Override
    public Void visitPrintStmt(Print stmt) {
        resolve(stmt.expression);
        return null;
    }

    @Override
    public Void visitExpressionStmt(Expression stmt) {
        resolve(stmt.expression);
        return null;
    }

    @Override
    public Void visitVarStmt(Var stmt) {
        declare(stmt.name);
        if (stmt.initializer != null)
            resolve(stmt.initializer);
        define(stmt.name);
        return null;
    }

    @Override
    public Void visitBlockStmt(Block stmt) {
        beginScope();
        resolve(stmt.statements);
        endScope();
        return null;
    }

    @Override
    public Void visitIfStmt(If stmt) {
        resolve(stmt.condition);
        resolve(stmt.thenStatement);
        if (stmt.elseStatement != null)
            resolve(stmt.elseStatement);
        return null;
    }

    @Override
    public Void visitWhileStmt(While stmt) {
        resolve(stmt.condition);
        resolve(stmt.body);
        return null;
    }

    @Override
    public Void visitFunctionStmt(Function stmt) {
        declare(stmt.name);
        define(stmt.name);
        resolveFunction(stmt, FunctionType.FUNCTION);
        return null;
    }

    @Override
    public Void visitReturnStmt(Return stmt) {
        if (currentFunction == FunctionType.NONE) {
            Lox.error(stmt.keyword, "Can't return from top-level code.");
        }
        if (stmt.value != null)
            resolve(stmt.value);
        return null;
    }

    @Override
    public Object visitBinaryExpr(Binary expr) {
        resolve(expr.left);
        resolve(expr.right);
        return null;
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        resolve(expr.expression);
        return null;
    }

    @Override
    public Object visitLiteralExpr(Literal expr) {
        return null;
    }

    @Override
    public Object visitLogicalExpr(Logical expr) {
        resolve(expr.left);
        resolve(expr.right);
        return null;
    }

    @Override
    public Object visitUnaryExpr(Unary expr) {
        resolve(expr.right);
        return null;
    }

    @Override
    public Object visitCallExpr(Call expr) {
        resolve(expr.callee);
        for (Expr arg : expr.arguments) {
            resolve(arg);
        }
        return null;
    }

    @Override
    public Object visitVariableExpr(Variable expr) {
        if (!scopes.isEmpty() && scopes.peek().get(expr.name.lexeme) == Boolean.FALSE) {
            Lox.error(expr.name, "Can't read local variable in it's own initializer");
        }
        resolveLocal(expr, expr.name);
        return null;
    }

    @Override
    public Object visitAssignExpr(Assign expr) {
        resolve(expr.value);
        resolveLocal(expr, expr.name);
        return null;
    }

}