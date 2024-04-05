package tinkerscript;

import java.util.List;

abstract class Stmt {

    interface Visitor<R> {
        R visitExpressionStmt(Expression stmt);

        R visitVarStmt(Var stmt);

        R visitBlockStmt(Block stmt);

        R visitIfStmt(If stmt);

        R visitWhileStmt(While stmt);

        R visitForStmt(For stmt);

        R visitFunctionStmt(Function stmt);

        R visitReturnStmt(Return stmt);

        R visitClassStmt(Class stmt);

        R visitTryCatchStmt(TryCatch stmt);

        R visitThrowStmt(Throw stmt);

        R visitBreakStmt(Break stmt);

        R visitContinueStmt(Continue stmt);

        R visitCaseStmt(Case stmt);

        R visitSwitchStmt(Switch stmt);

    }

    static class Expression extends Stmt {
        Expression(Expr expression) {
            this.expression = expression;
        }

        final Expr expression;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }
    }

    static class Var extends Stmt {
        Var(Token name, Expr initializer) {
            this.name = name;
            this.initializer = initializer;
        }

        final Token name;
        final Expr initializer;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStmt(this);
        }
    }

    static class Block extends Stmt {
        Block(List<Stmt> statements) {
            this.statements = statements;
        }

        final List<Stmt> statements;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBlockStmt(this);
        }
    }

    static class If extends Stmt {
        If(Expr condition , Stmt thenStatement , Stmt elseStatement) {
            this.condition = condition;
            this.thenStatement = thenStatement;
            this.elseStatement = elseStatement;
        }

        final Expr condition;
        final Stmt thenStatement;
        final Stmt elseStatement;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitIfStmt(this);
        }
    }

    static class While extends Stmt {
        While(Expr condition,  Stmt body) {
            this.condition = condition;
            this.body = body;
        }

        final Expr condition;
        final Stmt body;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitWhileStmt(this);
        }
    }

    static class For extends Stmt {
        For(Stmt initializer, Expr condition, Expr increment, Stmt body) {
            this.initializer = initializer;
            this.condition = condition;
            this.increment = increment;
            this.body = body;
        }

        final Stmt initializer;
        final Expr condition;
        final Expr increment;
        final Stmt body;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitForStmt(this);
        }
    }

    static class Function extends Stmt {
        Function(Token name, List<Token> params, Token spread , List<Stmt> body, Token staticToken, Boolean isGetter) {
            this.name = name;
            this.params = params;
            this.spread = spread;
            this.body = body;
            this.staticToken = staticToken;
            this.isGetter = isGetter;
        }

        final Token name;
        final List<Token> params;
        final Token spread;
        final List<Stmt> body;
        final Token staticToken;
        final Boolean isGetter;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitFunctionStmt(this);
        }
    }

    static class Return extends Stmt {
        Return(Token keyword, Expr value) {
            this.keyword = keyword;
            this.value = value;
        }

        final Token keyword;
        final Expr value;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitReturnStmt(this);
        }
    }

    static class Class extends Stmt {
        Class(Token name, List<Stmt.Function> methods, Expr.Variable superClass) {
            this.name = name;
            this.methods = methods;
            this.superClass = superClass;
        }

        final Token name;
        final List<Stmt.Function> methods;
        final Expr.Variable superClass;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitClassStmt(this);
        }
    }

    static class TryCatch extends Stmt {
        TryCatch(List<Stmt> tryBlock, List<Stmt> catchBlock, Token exception) {
            this.tryBlock = tryBlock;
            this.catchBlock = catchBlock;
            this.exception = exception;
        }

        final List<Stmt> tryBlock;
        final List<Stmt> catchBlock;
        final Token exception;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitTryCatchStmt(this);
        }
    }

    static class Throw extends Stmt {
        Throw(Token keyword, Expr value) {
            this.keyword = keyword;
            this.value = value;
        }

        final Token keyword;
        final Expr value;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitThrowStmt(this);
        }
    }

    static class Break extends Stmt {
        Break(Token keyword) {
            this.keyword = keyword;
        }

        final Token keyword;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBreakStmt(this);
        }
    }

    static class Continue extends Stmt {
        Continue(Token keyword) {
            this.keyword = keyword;
        }

        final Token keyword;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitContinueStmt(this);
        }
    }

    static class Case extends Stmt {
        Case(Expr value, List<Stmt> body) {
            this.value = value;
            this.body = body;
        }

        final Expr value;
        final List<Stmt> body;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitCaseStmt(this);
        }
    }

    static class Switch extends Stmt {
        Switch(Expr value, List<Stmt.Case> cases) {
            this.value = value;
            this.cases = cases;
        }

        final Expr value;
        final List<Stmt.Case> cases;

        <R> R accept(Visitor<R> visitor) {
            return visitor.visitSwitchStmt(this);
        }
    }

    abstract <R> R accept(Visitor<R> visitor);
}
