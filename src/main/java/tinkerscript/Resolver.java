package tinkerscript;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import tinkerscript.Expr.Array;
import tinkerscript.Expr.Assign;
import tinkerscript.Expr.Binary;
import tinkerscript.Expr.Call;
import tinkerscript.Expr.Dictionary;
import tinkerscript.Expr.Get;
import tinkerscript.Expr.Grouping;
import tinkerscript.Expr.KeyAccess;
import tinkerscript.Expr.KeySet;
import tinkerscript.Expr.Lambda;
import tinkerscript.Expr.Literal;
import tinkerscript.Expr.Logical;
import tinkerscript.Expr.PostFix;
import tinkerscript.Expr.PreFix;
import tinkerscript.Expr.Set;
import tinkerscript.Expr.Slice;
import tinkerscript.Expr.Spread;
import tinkerscript.Expr.Super;
import tinkerscript.Expr.Ternary;
import tinkerscript.Expr.This;
import tinkerscript.Expr.Unary;
import tinkerscript.Expr.Variable;
import tinkerscript.Stmt.Block;
import tinkerscript.Stmt.Break;
import tinkerscript.Stmt.Case;
import tinkerscript.Stmt.Class;
import tinkerscript.Stmt.Continue;
import tinkerscript.Stmt.Expression;
import tinkerscript.Stmt.For;
import tinkerscript.Stmt.Function;
import tinkerscript.Stmt.If;
import tinkerscript.Stmt.Return;
import tinkerscript.Stmt.Switch;
import tinkerscript.Stmt.Throw;
import tinkerscript.Stmt.TryCatch;
import tinkerscript.Stmt.Var;
import tinkerscript.Stmt.While;

public class Resolver implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

    private final Stack<Map<String, Boolean>> scopes = new Stack<>();
    private final Interpreter interpreter;
    public Boolean hadError = false;

    private enum FunctionType {
        NONE,
        FUNCTION,
        METHOD,
        INITIALIZER
    };

    private enum ClassType {
        NONE,
        CLASS,
        SUBCLASS,
    }

    private enum LoopType {
        NONE,
        LOOP
    }

    private enum SwitchType {
        NONE,
        SWITCH
    }

    private ClassType currentClass = ClassType.NONE;

    private FunctionType currentFunction = FunctionType.NONE;

    private LoopType currentLoop = LoopType.NONE;
    private SwitchType currentSwitch = SwitchType.NONE;

    Resolver(Interpreter interpreter) {
        this.interpreter = interpreter;
    }

    void error(Token token, String message) {
        Compiler.error(token, message);
        hadError = true;
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
            error(name, "Already a variable with this name in this scope.");
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
                return;
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
        LoopType enclosingLoop = currentLoop;
        currentLoop = LoopType.LOOP;
        resolve(stmt.body);
        currentLoop = enclosingLoop;
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
            error(stmt.keyword, "Can't return from top-level code.");
        }
        if (stmt.value != null) {
            if (currentFunction == FunctionType.INITIALIZER) {
                error(stmt.keyword, "Can't return a value from an initializer.");
            }
            resolve(stmt.value);
        }
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
            error(expr.name, "Can't read local variable in its own initializer.");
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

    @Override
    public Void visitClassStmt(Class stmt) {
        if (stmt.name.lexeme == "Object") {
            error(stmt.name, "Can't declare a class with name 'Object'.");
        }
        ClassType enclosingClass = currentClass;
        currentClass = ClassType.CLASS;
        if (stmt.superClass != null)
            currentClass = ClassType.SUBCLASS;
        declare(stmt.name);
        define(stmt.name);
        if (stmt.superClass != null) {
            if (stmt.name.lexeme.equals(stmt.superClass.name.lexeme)) {
                error(stmt.superClass.name, "A class can't inherit from itself.");
            }
            resolve(stmt.superClass);
        }
        // resolve static methods
        for (Stmt.Function function : stmt.methods) {
            if (function.staticToken != null) {
                if (function.isGetter) {
                    error(function.staticToken, "Getters can't be static.");
                }
                resolveFunction(function, FunctionType.METHOD);
            }
        }
        if (stmt.superClass != null) {
            beginScope();
            scopes.peek().put("super", true);
        }
        beginScope();
        scopes.peek().put("this", true);
        for (Stmt.Function function : stmt.methods) {
            if (function.staticToken == null) {
                if (function.name.lexeme.equals("init")) {
                    if (function.isGetter) {
                        error(function.name, "Getter method's name can't be 'init'.");
                    }
                    resolveFunction(function, FunctionType.INITIALIZER);
                } else {
                    resolveFunction(function, FunctionType.METHOD);
                }
            }
        }
        endScope();
        if (stmt.superClass != null)
            endScope();
        currentClass = enclosingClass;
        return null;
    }

    @Override
    public Object visitGetExpr(Get expr) {
        resolve(expr.object);
        return null;
    }

    @Override
    public Object visitSetExpr(Set expr) {
        resolve(expr.object);
        resolve(expr.value);
        return null;
    }

    @Override
    public Object visitThisExpr(This expr) {
        if (currentClass == ClassType.NONE) {
            error(expr.keyword, "Can't use 'this' outside of a class.");
        }
        resolveLocal(expr, expr.keyword);
        return null;
    }

    @Override
    public Object visitSuperExpr(Super expr) {
        if (currentClass == ClassType.NONE) {
            error(expr.keyword, "Can't use 'super' outside of a class.");
        } else if (currentClass != ClassType.SUBCLASS) {
            error(expr.keyword, "Can't use 'super' in a class with no superclass.");
        }
        resolveLocal(expr, expr.keyword);
        return null;
    }

    @Override
    public Object visitTernaryExpr(Ternary expr) {
        resolve(expr.condition);
        resolve(expr.left);
        resolve(expr.right);
        return null;
    }

    @Override
    public Object visitArrayExpr(Array expr) {
        for (Expr element : expr.elements) {
            resolve(element);
        }
        return null;
    }

    @Override
    public Object visitKeyAccessExpr(KeyAccess expr) {
        resolve(expr.object);
        resolve(expr.key);
        return null;
    }

    @Override
    public Object visitKeySetExpr(KeySet expr) {
        resolve(expr.object);
        resolve(expr.key);
        resolve(expr.value);
        return null;
    }

    @Override
    public Object visitPostFixExpr(PostFix expr) {
        resolve(expr.left);
        return null;
    }

    @Override
    public Object visitPreFixExpr(PreFix expr) {
        resolve(expr.right);
        return null;
    }

    @Override
    public Object visitDictionaryExpr(Dictionary expr) {
        for (Expr key : expr.keys) {
            resolve(key);
        }
        for (Expr value : expr.values) {
            resolve(value);
        }
        return null;
    }

    @Override
    public Object visitSpreadExpr(Spread expr) {
        resolve(expr.right);
        return null;
    }

    @Override
    public Void visitTryCatchStmt(TryCatch stmt) {
        beginScope();
        resolve(stmt.tryBlock);
        endScope();
        beginScope();
        declare(stmt.exception);
        define(stmt.exception);
        resolve(stmt.catchBlock);
        endScope();
        return null;
    }

    @Override
    public Void visitThrowStmt(Throw stmt) {
        resolve(stmt.value);
        return null;
    }

    @Override
    public Void visitBreakStmt(Break stmt) {
        if (currentLoop == LoopType.NONE && currentSwitch == SwitchType.NONE)
            error(stmt.keyword, "Can't use 'break' outside of a loop, switch.");
        return null;
    }

    @Override
    public Void visitContinueStmt(Continue stmt) {
        if (currentLoop == LoopType.NONE)
            error(stmt.keyword, "Can't use 'continue' outside of a loop.");
        return null;
    }

    @Override
    public Void visitForStmt(For stmt) {
        beginScope();
        if (stmt.initializer != null)
            resolve(stmt.initializer);
        if (stmt.condition != null)
            resolve(stmt.condition);
        if (stmt.increment != null)
            resolve(stmt.increment);
        LoopType enclosingLoop = currentLoop;
        currentLoop = LoopType.LOOP;
        resolve(stmt.body);
        currentLoop = enclosingLoop;
        endScope();
        return null;
    }

    @Override
    public Object visitFunctionExpr(tinkerscript.Expr.Function expr) {
        beginScope();
        if (expr.function.name != null) {
            declare(expr.function.name);
            define(expr.function.name);
        }
        resolveFunction(expr.function, FunctionType.FUNCTION);
        endScope();
        return null;
    }

    @Override
    public Object visitLambdaExpr(Lambda expr) {
        FunctionType enclosingFunction = currentFunction;
        currentFunction = FunctionType.FUNCTION;
        beginScope();

        for (Token param : expr.params) {
            declare(param);
            define(param);
        }
        resolve(expr.body);
        endScope();
        currentFunction = enclosingFunction;
        return null;
    }

    @Override
    public Void visitCaseStmt(Case stmt) {
        if (stmt.value != null)
            resolve(stmt.value);
        resolve(stmt.body);
        return null;
    }

    @Override
    public Void visitSwitchStmt(Switch stmt) {
        resolve(stmt.value);
        SwitchType enclosingSwitch = currentSwitch;
        currentSwitch = SwitchType.SWITCH;
        beginScope();
        for (Case caseStmt : stmt.cases) {
            resolve(caseStmt);
        }
        endScope();
        currentSwitch = enclosingSwitch;
        return null;
    }

    @Override
    public Object visitSliceExpr(Slice expr) {
        resolve(expr.array);
        if (expr.start != null)
            resolve(expr.start);
        if (expr.end != null)
            resolve(expr.end);
        return null;
    }

}
