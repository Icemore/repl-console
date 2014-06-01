package spbau.oop.expr.exp;

import spbau.oop.expr.visit.ExpVisitor;
import spbau.oop.expr.visit.VisitorFailException;

public class Sub extends BiExp {
    public Sub(Exp left, Exp right) {
        super(left, right);
    }

    @Override
    public <T> T accept(ExpVisitor<T> visitor) throws VisitorFailException {
        return visitor.visit(this);
    }

    @Override
    public <T> void traverse(ExpVisitor<T> visitor) throws VisitorFailException {
        left.traverse(visitor);
        visitor.visit(this);
        right.traverse(visitor);
    }
}
