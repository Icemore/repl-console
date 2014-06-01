package spbau.oop.expr.exp;

import spbau.oop.expr.visit.ExpVisitor;
import spbau.oop.expr.visit.VisitorFailException;

public class Sum extends BiExp {
    public Sum(Exp left, Exp right) {
        super(left, right);
    }

    @Override
    public <T> T accept(ExpVisitor<T> prettyPrinter) throws VisitorFailException {
        return prettyPrinter.visit(this);
    }

    @Override
    public <T> void traverse(ExpVisitor<T> visitor) throws VisitorFailException {
        left.traverse(visitor);
        visitor.visit(this);
        right.traverse(visitor);
    }
}
