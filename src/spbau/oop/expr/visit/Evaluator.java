package spbau.oop.expr.visit;

import spbau.oop.expr.exp.*;

import java.util.Map;

public class Evaluator implements ExpVisitor<Exp> {
    private Map<String, Number> context;
    private boolean evalVariables;

    public Evaluator(Map<String, Number> context, boolean evalVariables) {
        this.context = context;
        this.evalVariables = evalVariables;
    }

    @Override
    public Exp visit(Num num) {
        return num;
    }

    @Override
    public Exp visit(Sum sum) throws VisitorFailException {
        Exp left = sum.left.accept(this);
        Exp right = sum.right.accept(this);

        if ((left instanceof Num) && (right instanceof Num)) {
            Number n = ((Num) left).number.intValue() + ((Num) right).number.intValue();
            return new Num(n, sum.getSpan());
        } else {
            return new Sum(left, right);
        }
    }

    @Override
    public Exp visit(Mul mul) throws VisitorFailException {
        Exp left = mul.left.accept(this);
        Exp right = mul.right.accept(this);

        if ((left instanceof Num) && (right instanceof Num)) {
            Number n = ((Num) left).number.intValue() * ((Num) right).number.intValue();
            return new Num(n, mul.getSpan());
        } else {
            return new Mul(left, right);
        }
    }

    @Override
    public Exp visit(Div div) throws VisitorFailException {
        Exp left = div.left.accept(this);
        Exp right = div.right.accept(this);

        if ((left instanceof Num) && (right instanceof Num)) {
            Number n = ((Num) left).number.intValue() / ((Num) right).number.intValue();
            return new Num(n, div.getSpan());
        } else {
            return new Div(left, right);
        }
    }

    @Override
    public Exp visit(Variable var) throws VisitorFailException {
        if (!evalVariables) {
            return var;
        } else {
            String name = var.name;
            if (!context.containsKey(name)) {
                throw new VisitorFailException("Undefined variable " + name);
            }

            return new Num(context.get(name), var.getSpan());
        }
    }

    @Override
    public Exp visit(Assignment ass) throws VisitorFailException {
        Variable var = (Variable) ass.left;
        Exp expr = ass.right.accept(this);

        return new Assignment(var, expr);
    }

    @Override
    public Exp visit(Missing miss) throws VisitorFailException {
        return miss;
    }

    @Override
    public Exp visit(Sub sub) throws VisitorFailException {
        Exp left = sub.left.accept(this);
        Exp right = sub.right.accept(this);

        if ((left instanceof Num) && (right instanceof Num)) {
            Number n = ((Num) left).number.intValue() - ((Num) right).number.intValue();
            return new Num(n, sub.getSpan());
        } else {
            return new Sub(left, right);
        }
    }
}
