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
import com.sravan.lox.Expr.ObjectLiteral;
import com.sravan.lox.Expr.Get;
import com.sravan.lox.Expr.Grouping;
import com.sravan.lox.Expr.Literal;
import com.sravan.lox.Expr.Logical;
import com.sravan.lox.Expr.PostFix;
import com.sravan.lox.Expr.PreFix;
import com.sravan.lox.Expr.Set;
import com.sravan.lox.Expr.Spread;
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
        globals.define("clock", new NativeFunction.Clock());
        globals.define("strlen", new NativeFunction.StringLength());
        globals.define("read", new NativeFunction.Input());
        globals.define("len", new NativeFunction.ArrayLength());
        globals.define("number", new NativeFunction.ToNumber());
        globals.define("string", new NativeFunction.ToString());

        // define a base class Object which is superclass of all classes
        LoxClass objectClass = new LoxClass("Object", new HashMap<>(), null);
        globals.define("Object", objectClass);
        LoxClass arrayClass = new LoxClass("Array", new HashMap<>(), objectClass);
        globals.define("Array", arrayClass);
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
        System.out.print(Lox.stringify(value));
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
        if (expr.operator.type == OR || expr.operator.type == PIPE_PIPE) {
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
        LoxFunction function = new UserFunction(stmt, environment, false);
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
        } else {
            superClass = globals.get("Object");
        }
        environment.define(stmt.name.lexeme, null);
        if (superClass != null) {
            environment = new Environment(environment);
            environment.define("super", superClass);
        }
        Map<String, LoxFunction> methods = new HashMap<>();
        for (Stmt.Function method : stmt.methods) {
            LoxFunction function = new UserFunction(method, environment, method.name.lexeme.equals("init"));
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
            Object value = evaluate(element);
            if (element instanceof Expr.Spread) {
                if (value instanceof List<?>) {
                    elements.addAll((List<?>) value);
                }
            } else {
                elements.add(value);
            }
        }
        Object klass = globals.get("Array");
        return new LoxArray((LoxClass) klass, elements);
    }

    @Override
    public Object visitArrayAccessExpr(ArrayAccess expr) {
        Object array = evaluate(expr.array);
        Object index = evaluate(expr.index);
        if (!checkInteger(index)) {
            throw new RuntimeError(expr.rightSqParen, "Index must be an integer");
        }
        int indexValue = (int) ((double) index);
        if ((array instanceof LoxArray)) {
            return ((LoxArray) array).get(expr.rightSqParen, indexValue);
        }
        if (array instanceof String) {
            if (indexValue >= ((String) array).length()) {
                throw new RuntimeError(expr.rightSqParen, "Index " + indexValue + " out of range");
            }
            return String.valueOf(((String) array).charAt(indexValue));
        }
        throw new RuntimeError(expr.rightSqParen, "Only arrays or strings can be accessed through [] notation");
    }

    @Override
    public Object visitArraySetExpr(ArraySet expr) {
        Object array = evaluate(expr.array);
        Object index = evaluate(expr.index);
        Object value = evaluate(expr.value);
        int indexValue = (int) ((double) index);
        if (!checkInteger(index)) {
            throw new RuntimeError(expr.equals, "array index must be an integer");
        }
        if ((array instanceof LoxArray)) {
            ((LoxArray) array).set(expr.equals, indexValue, value);
            return value;
        }
        if (array instanceof String) {
            throw new RuntimeError(expr.equals, "Strings are immutable");
        }
        throw new RuntimeError(expr.equals, "Only arrays can be accessed through [] notation");
    }

    @Override
    public Object visitPostFixExpr(PostFix expr) {
        Object value = evaluate(expr.left);
        if (expr.operator.type == PLUS_PLUS) {
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

    @Override
    public Object visitObjectLiteralExpr(ObjectLiteral expr) {
        List<Token> keys = expr.keys;
        List<Expr> values = expr.values;
        List<Object> evaluatedValues = new ArrayList<>();
        for (Expr value : values) {
            evaluatedValues.add(evaluate(value));
        }
        LoxClass klass = (LoxClass) globals.get("Object");
        LoxInstance instance = new LoxInstance(klass);
        for (int i = 0; i < keys.size(); i++) {
            instance.set(keys.get(i), evaluatedValues.get(i));
        }
        return instance;
    }

    @Override
    public Object visitSpreadExpr(Spread expr) {
        Object value = evaluate(expr.right);
        if ((value instanceof LoxArray)) {
            return ((LoxArray) value).elements;
        }
        throw new RuntimeError(expr.operator, "Only arrays can be spread");
    }
}
