package spbau.oop.expr.visit;

import spbau.oop.expr.exp.*;

public abstract class ExpVisitorAdapter implements ExpVisitor<Void> {
    @Override
    public Void visit(Num num) {
        return null;
    }

    @Override
    public Void visit(Sum sum) {
        return null;
    }

    @Override
    public Void visit(Mul mul) {
        return null;
    }

    @Override
    public Void visit(Div div) throws VisitorFailException {
        return null;
    }

    @Override
    public Void visit(Variable var) {
        return null;
    }

    @Override
    public Void visit(Assignment ass) {
        return null;
    }
}
