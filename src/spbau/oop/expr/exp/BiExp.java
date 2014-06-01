package spbau.oop.expr.exp;

import spbau.oop.expr.parsing.Span;

public abstract class BiExp extends AbstractExp {
    public final Exp left;
    public final Exp right;

    public BiExp(Exp left, Exp right) {
        super(new Span(left.getSpan().getStart(), right.getSpan().getEnd()));
        this.left = left;
        this.right = right;
    }
}
