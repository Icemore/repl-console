package spbau.oop.expr.exp;

import spbau.oop.expr.parsing.Span;

public abstract class AbstractExp implements Exp {
    private Span span;

    protected AbstractExp(Span span) {
        this.span = span;
    }

    public Span getSpan() {
        return span;
    }
}
