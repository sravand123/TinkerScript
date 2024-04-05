package tinkerscript;

import java.util.List;

abstract class Expr {

    interface Visitor<R> {
        R visitBinaryExpr(Binary expr);

        R visitGroupingExpr(Grouping expr);

        R visitLiteralExpr(Literal expr);

        R visitLogicalExpr(Logical expr);

        R visitUnaryExpr(Unary expr);

        R visitPostFixExpr(PostFix expr);

        R visitPreFixExpr(PreFix expr);

        R visitCallExpr(Call expr);

        R visitVariableExpr(Variable expr);

        R visitAssignExpr(Assign expr);

        R visitGetExpr(Get expr);

        R visitSetExpr(Set expr);

        R visitThisExpr(This expr);

        R visitSuperExpr(Super expr);

        R visitTernaryExpr(Ternary expr);

        R visitArrayExpr(Array expr);

        R visitKeyAccessExpr(KeyAccess expr);

        R visitKeySetExpr(KeySet expr);

        R visitSliceExpr(Slice expr);

        R visitDictionaryExpr(Dictionary expr);

        R visitSpreadExpr(Spread expr);

        R visitFunctionExpr(Function expr);

        R visitLambdaExpr(Lambda expr);

    }

    static class Binary extends Expr {
        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        final Expr left;
        final Token operator;
        final Expr right;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    static class Grouping extends Expr {
        Grouping(Expr expression) {
            this.expression = expression;
        }

        final Expr expression;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
    }

    static class Literal extends Expr {
        Literal(Object value) {
            this.value = value;
        }

        final Object value;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
    }

    static class Logical extends Expr {
        Logical(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        final Expr left;
        final Token operator;
        final Expr right;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLogicalExpr(this);
        }
    }

    static class Unary extends Expr {
        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        final Token operator;
        final Expr right;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

    static class PostFix extends Expr {
        PostFix(Token operator, Expr left) {
            this.operator = operator;
            this.left = left;
        }

        final Token operator;
        final Expr left;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPostFixExpr(this);
        }
    }

    static class PreFix extends Expr {
        PreFix(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        final Token operator;
        final Expr right;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPreFixExpr(this);
        }
    }

    static class Call extends Expr {
        Call(Expr callee , Token paren, List<Expr> arguments) {
            this.callee = callee;
            this.paren = paren;
            this.arguments = arguments;
        }

        final Expr callee;
        final Token paren;
        final List<Expr> arguments;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitCallExpr(this);
        }
    }

    static class Variable extends Expr {
        Variable(Token name) {
            this.name = name;
        }

        final Token name;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariableExpr(this);
        }
    }

    static class Assign extends Expr {
        Assign(Token name, Expr value) {
            this.name = name;
            this.value = value;
        }

        final Token name;
        final Expr value;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitAssignExpr(this);
        }
    }

    static class Get extends Expr {
        Get(Expr object, Token name) {
            this.object = object;
            this.name = name;
        }

        final Expr object;
        final Token name;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGetExpr(this);
        }
    }

    static class Set extends Expr {
        Set(Expr object, Token name, Expr value) {
            this.object = object;
            this.name = name;
            this.value = value;
        }

        final Expr object;
        final Token name;
        final Expr value;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitSetExpr(this);
        }
    }

    static class This extends Expr {
        This(Token keyword) {
            this.keyword = keyword;
        }

        final Token keyword;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitThisExpr(this);
        }
    }

    static class Super extends Expr {
        Super(Token keyword , Token method) {
            this.keyword = keyword;
            this.method = method;
        }

        final Token keyword;
        final Token method;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitSuperExpr(this);
        }
    }

    static class Ternary extends Expr {
        Ternary(Expr condition , Expr left, Expr right) {
            this.condition = condition;
            this.left = left;
            this.right = right;
        }

        final Expr condition;
        final Expr left;
        final Expr right;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitTernaryExpr(this);
        }
    }

    static class Array extends Expr {
        Array(List<Expr> elements) {
            this.elements = elements;
        }

        final List<Expr> elements;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitArrayExpr(this);
        }
    }

    static class KeyAccess extends Expr {
        KeyAccess(Expr object, Expr key, Token rightSqParen) {
            this.object = object;
            this.key = key;
            this.rightSqParen = rightSqParen;
        }

        final Expr object;
        final Expr key;
        final Token rightSqParen;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitKeyAccessExpr(this);
        }
    }

    static class KeySet extends Expr {
        KeySet(Expr object, Expr key, Expr value , Token equals) {
            this.object = object;
            this.key = key;
            this.value = value;
            this.equals = equals;
        }

        final Expr object;
        final Expr key;
        final Expr value;
        final Token equals;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitKeySetExpr(this);
        }
    }

    static class Slice extends Expr {
        Slice(Expr array, Expr start, Expr end,  Token rightSqParen) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.rightSqParen = rightSqParen;
        }

        final Expr array;
        final Expr start;
        final Expr end;
        final Token rightSqParen;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitSliceExpr(this);
        }
    }

    static class Dictionary extends Expr {
        Dictionary(List<Expr> keys, List<Expr> values) {
            this.keys = keys;
            this.values = values;
        }

        final List<Expr> keys;
        final List<Expr> values;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitDictionaryExpr(this);
        }
    }

    static class Spread extends Expr {
        Spread(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        final Token operator;
        final Expr right;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitSpreadExpr(this);
        }
    }

    static class Function extends Expr {
        Function(Stmt.Function function) {
            this.function = function;
        }

        final Stmt.Function function;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitFunctionExpr(this);
        }
    }

    static class Lambda extends Expr {
        Lambda(List<Token> params, Token spread , Expr body) {
            this.params = params;
            this.spread = spread;
            this.body = body;
        }

        final List<Token> params;
        final Token spread;
        final Expr body;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLambdaExpr(this);
        }
    }

    abstract <R> R accept(Visitor<R> visitor);
}
