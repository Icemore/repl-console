package spbau.oop.expr.exp;

import spbau.oop.expr.parsing.Span;
import spbau.oop.expr.visit.ExpVisitor;
import spbau.oop.expr.visit.VisitorFailException;

public interface Exp {
    public Span getSpan();

    public <T> T accept(ExpVisitor<T> visitor) throws VisitorFailException;

    public <T> void traverse(ExpVisitor<T> visitor) throws VisitorFailException;
}
