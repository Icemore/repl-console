package spbau.oop.expr.exp;

import spbau.oop.expr.parsing.Span;
import spbau.oop.expr.visit.ExpVisitor;
import spbau.oop.expr.visit.VisitorFailException;

public class Missing extends AbstractExp {
    public Missing(Span span) {
        super(span);
    }

    @Override
    public <T> T accept(ExpVisitor<T> visitor) throws VisitorFailException {
        return visitor.visit(this);
    }

    @Override
    public <T> void traverse(ExpVisitor<T> visitor) throws VisitorFailException {
        visitor.visit(this);
    }
}
