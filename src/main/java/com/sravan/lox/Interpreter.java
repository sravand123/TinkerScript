package com.sravan.lox;

import static com.sravan.lox.TokenType.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sravan.lox.Expr.Array;
import com.sravan.lox.Expr.ArrayAccess;
import com.sravan.lox.Expr.ArraySet;
import com.sravan.lox.Expr.Assign;
import com.sravan.lox.Expr.Binary;
import com.sravan.lox.Expr.Call;
import com.sravan.lox.Expr.Get;
import com.sravan.lox.Expr.Grouping;
import com.sravan.lox.Expr.Literal;
import com.sravan.lox.Expr.Logical;
import com.sravan.lox.Expr.PostFix;
import com.sravan.lox.Expr.PreFix;
import com.sravan.lox.Expr.Set;
import com.sravan.lox.Expr.Super;
import com.sravan.lox.Expr.Ternary;
import com.sravan.lox.Expr.This;
import com.sravan.lox.Expr.Unary;
import com.sravan.lox.Expr.Variable;
import com.sravan.lox.Stmt.Block;
import com.sravan.lox.Stmt.Class;
import com.sravan.lox.Stmt.Expression;
import com.sravan.lox.Stmt.Function;
import com.sravan.lox.Stmt.If;
import com.sravan.lox.Stmt.Print;
import com.sravan.lox.Stmt.Var;
import com.sravan.lox.Stmt.While;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
    final Environment globals = new Environment();
    private Environment environment = globals;
    private final Map<Expr, Integer> locals = new HashMap<>();

    Interpreter() {
        globals.define("clock", new LoxCallable() {

            @Override
            public int arity() {
                return 0;
            }

            @Override
            public Object call(Interpreter interpreter, List<Object> arguments) {
                return (double) System.currentTimeMillis() / 1000.0;
            }

            @Override
            public String toString() {
                return "<native function>";
            }

        });
    }

    void resolve(Expr expression, Integer depth) {
        locals.put(expression, depth);
    }

    void interpret(List<Stmt> statements) {
        try {
            for (Stmt stmt : statements) {
                execute(stmt);
            }
        } catch (RuntimeError error) {
            Lox.runtimeError(error);
        }
    }

    void execute(Stmt statement) {
        statement.accept(this);
    }

    @Override
    public Object visitBinaryExpr(Binary expr) {
        Object leftVal = evaluate(expr.left);
        Object rightVal = evaluate(expr.right);
        switch (expr.operator.type) {
            case STAR:
                checkNumberOperands(expr.operator, leftVal, rightVal);
                return (double) leftVal * (double) rightVal;
            case SLASH:
                checkNumberOperands(expr.operator, leftVal, rightVal);
                return (double) leftVal / (double) rightVal;
            case MINUS:
                checkNumberOperands(expr.operator, leftVal, rightVal);
                return (double) leftVal - (double) rightVal;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, leftVal, rightVal);
                return (double) leftVal <= (double) rightVal;
            case LESS:
                checkNumberOperands(expr.operator, leftVal, rightVal);
                return (double) leftVal < (double) rightVal;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, leftVal, rightVal);
                return (double) leftVal >= (double) rightVal;
            case GREATER:
                checkNumberOperands(expr.operator, leftVal, rightVal);
                return (double) leftVal > (double) rightVal;
            case EQUAL_EQUAL:
                return isEqual(leftVal, rightVal);
            case BANG_EQUAL:
                return !isEqual(leftVal, rightVal);
            case PIPE:
                checkIntegerOperands(expr.operator, leftVal, rightVal);
                return (double) ((int) (double) leftVal | (int) (double) rightVal);
            case AMPERSAND:
                checkIntegerOperands(expr.operator, leftVal, rightVal);
                return (double) ((int) (double) leftVal & (int) (double) rightVal);
            case CARAT:
                checkIntegerOperands(expr.operator, leftVal, rightVal);
                return (double) ((int) (double) leftVal ^ (int) (double) rightVal);
            case PLUS:
                if (leftVal instanceof Double && rightVal instanceof Double) {
                    return (double) leftVal + (double) rightVal;
                }
                if (leftVal instanceof String && rightVal instanceof String) {
                    return (String) leftVal + (String) rightVal;
                }
                throw new RuntimeError(expr.operator, "Operands must be two numbers or two string");
            default:
                return null;
        }
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        return (evaluate(expr.expression));

    }

    @Override
    public Object visitLiteralExpr(Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpr(Unary expr) {
        Object right = evaluate(expr.right);
        switch (expr.operator.type) {
            case MINUS:
                checkNumberOperand(expr.operator, right);
                return -(double) right;
            case BANG:
                return !isTruthy(right);
            case TILDA:
                checkIntegerOperand(expr.operator, right);
                return (double) (~(int) (double) right);
            default:
                return null;

        }
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean isTruthy(Object value) {
        if (value == null)
            return false;
        if (value instanceof Double) {
            return (Double) value != 0;
        }
        if (value instanceof String) {
            return !((String) value).equals("");
        }
        if (value instanceof Boolean)
            return (boolean) value;
        return true;

    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null)
            return true;
        if (a == null)
            return false;
        return a.equals(b);
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double)
            return;
        throw new RuntimeError(operator, "Operands must be numbers");
    }

    private boolean checkInteger(Object value) {
        if (value instanceof Integer)
            return true;
        if (!(value instanceof Double)) {
            return false;
        }
        Double doubleValue = (Double) value;
        if (doubleValue != doubleValue.intValue()) {
            return false;
        }
        return true;
    }

    private void checkIntegerOperands(Token operator, Object left, Object right) {
        if (checkInteger(left) && checkInteger(right))
            return;
        throw new RuntimeError(operator, "Operands must be integers");
    }

    private void checkIntegerOperand(Token operator, Object val) {
        if (checkInteger(val))
            return;
        throw new RuntimeError(operator, "Operand must be integer");
    }
    private void checkNumberOperand(Token operator, Object right) {
        if (right instanceof Double)
            return;
        throw new RuntimeError(operator, "Operand must be a number");
    }

    private String stringify(Object value) {
        if (value == null)
            return "nil";
        String text = value.toString();
        if (value instanceof Double) {
            if (text.endsWith(".0")) {
                return text.substring(0, text.length() - 2);
            }
            return text;
        }
        return text;
    }

    public Object lookUpVariable(Token name, Expr expr) {
        Integer distance = locals.get(expr);
        if (distance != null) {
            return environment.getAt(name.lexeme, distance);
        } else {
            return globals.get(name);
        }
    }

    @Override
    public Void visitPrintStmt(Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }

    @Override
    public Void visitExpressionStmt(Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Void visitVarStmt(Var stmt) {
        Object value = null;
        if (stmt.initializer != null)
            value = evaluate(stmt.initializer);
        environment.define(stmt.name.lexeme, value);
        return null;

    }

    @Override
    public Object visitVariableExpr(Variable expr) {
        return lookUpVariable(expr.name, expr);
    }

    @Override
    public Object visitAssignExpr(Assign expr) {
        Object value = evaluate(expr.value);
        Integer distance = locals.get(expr);
        if (distance != null) {
            environment.assignAt(expr.name, value, distance);
        } else {
            globals.assign(expr.name, value);
        }
        return value;
    }

    @Override
    public Void visitBlockStmt(Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }

    void executeBlock(List<Stmt> statements, Environment environment) {
        Environment previous = this.environment;
        try {
            this.environment = environment;
            for (Stmt stmt : statements) {
                execute(stmt);
            }
        } finally {
            this.environment = previous;
        }

    }

    @Override
    public Void visitIfStmt(If stmt) {
        boolean isConditionTruthy = isTruthy(evaluate(stmt.condition));
        if (isConditionTruthy)
            execute(stmt.thenStatement);
        else if (stmt.elseStatement != null)
            execute(stmt.elseStatement);
        return null;
    }

    @Override
    public Object visitLogicalExpr(Logical expr) {
        Object value = evaluate(expr.left);
        if (expr.operator.type == OR) {
            if (isTruthy(value))
                return true;
        } else {
            if (!isTruthy(value))
                return false;
        }
        return evaluate(expr.right);
    }

    @Override
    public Void visitWhileStmt(While stmt) {
        while (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.body);
        }
        return null;
    }

    @Override
    public Object visitCallExpr(Call expr) {
        Object callee = evaluate(expr.callee);
        List<Object> arguments = new ArrayList<>();
        for (Expr argument : expr.arguments) {
            arguments.add(evaluate(argument));
        }
        if (!(callee instanceof LoxCallable)) {
            throw new RuntimeError(expr.paren, "Can only call functions and classes");
        }
        LoxCallable function = (LoxCallable) callee;
        if (arguments.size() != function.arity()) {
            throw new RuntimeError(expr.paren,
                    "Expected " + function.arity() + " arguments but found " + arguments.size());
        }
        return function.call(this, arguments);
    }

    @Override
    public Void visitFunctionStmt(Function stmt) {
        LoxFunction function = new LoxFunction(stmt, environment, false);
        environment.define(stmt.name.lexeme, function);
        return null;
    }

    @Override
    public Void visitReturnStmt(Stmt.Return stmt) {
        Object value = null;
        if (stmt.value != null)
            value = evaluate(stmt.value);
        throw new Return(value);
    }

    @Override
    public Void visitClassStmt(Class stmt) {
        Object superClass = null;
        if (stmt.superClass != null) {
            superClass = evaluate(stmt.superClass);
            if (!(superClass instanceof LoxClass)) {
                throw new RuntimeError(stmt.superClass.name, "Superclass must be a class");
            }
        }
        environment.define(stmt.name.lexeme, null);
        if (stmt.superClass != null) {
            environment = new Environment(environment);
            environment.define("super", superClass);
        }
        Map<String, LoxFunction> methods = new HashMap<>();
        for (Stmt.Function method : stmt.methods) {
            LoxFunction function = new LoxFunction(method, environment, method.name.lexeme.equals("init"));
            methods.put(method.name.lexeme, function);
        }
        LoxClass klass = new LoxClass(stmt.name.lexeme, methods, (LoxClass) superClass);
        if (superClass != null) {
            environment = environment.enclosing;
        }
        environment.assign(stmt.name, klass);
        return null;
    }

    @Override
    public Object visitGetExpr(Get expr) {
        Object value = evaluate(expr.object);
        if (value instanceof LoxInstance) {
            return ((LoxInstance) value).get(expr.name);
        }
        throw new RuntimeError(expr.name, "Only instances have properties");
    }

    @Override
    public Object visitSetExpr(Set expr) {
        Object object = evaluate(expr.object);
        if (!(object instanceof LoxInstance)) {
            throw new RuntimeError(expr.name, "Only instances have feilds");
        }
        Object value = evaluate(expr.value);
        ((LoxInstance) object).set(expr.name, value);
        return value;
    }

    @Override
    public Object visitThisExpr(This expr) {
        return lookUpVariable(expr.keyword, expr);
    }

    @Override
    public Object visitSuperExpr(Super expr) {
        int distance = locals.get(expr);
        LoxClass superClass = (LoxClass) environment.getAt("super", distance);
        LoxInstance object = (LoxInstance) environment.getAt("this", distance - 1);
        LoxFunction method = superClass.findMethod(expr.method.lexeme);
        if (method == null) {
            throw new RuntimeError(expr.method, "Undefined propert '" + expr.method.lexeme + "'.");
        }
        return method.bind(object);
    }

    @Override
    public Object visitTernaryExpr(Ternary expr) {
        if (isTruthy(evaluate(expr.condition))) {
            return evaluate(expr.left);
        }
        return evaluate(expr.right);
    }

    @Override
    public Object visitArrayExpr(Array expr) {
        List<Object> elements = new ArrayList<>();
        for (Expr element : expr.elements) {
            elements.add(evaluate(element));
        }
        return new LoxArray(elements);
    }

    @Override
    public Object visitArrayAccessExpr(ArrayAccess expr) {
        Object array = evaluate(expr.array);
        Object index = evaluate(expr.index);
        if (!(array instanceof LoxArray)) {
            throw new RuntimeError(expr.rightSqParen, "Only arrays can be accessed through [] notation");
        }

        if (!checkInteger(index)) {
            throw new RuntimeError(expr.rightSqParen, "array index must be an integer");
        }
        return ((LoxArray) array).get(expr.rightSqParen, (int) ((double) index));
    }

    @Override
    public Object visitArraySetExpr(ArraySet expr) {
        Object array = evaluate(expr.array);
        Object index = evaluate(expr.index);
        Object value = evaluate(expr.value);
        if (!(array instanceof LoxArray)) {
            throw new RuntimeError(expr.equals, "Only arrays can be accessed through [] notation");
        }

        if (!checkInteger(index)) {
            throw new RuntimeError(expr.equals, "array index must be an integer");
        }
        ((LoxArray) array).set(expr.equals, (int) ((double) index), value);
        return value;
    }

    @Override
    public Object visitPostFixExpr(PostFix expr) {
        Object value = evaluate(expr.left);
        if (expr.operator.type == INCREMENT) {
            return (Double) value - 1;
        } else {
            return (Double) value + 1;
        }
    }

    @Override
    public Object visitPreFixExpr(PreFix expr) {
        Object value = evaluate(expr.right);
        return value;
    }
}
