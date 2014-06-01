package spbau.oop.expr.exp;

import spbau.oop.expr.parsing.Span;
import spbau.oop.expr.parsing.Token;
import spbau.oop.expr.visit.ExpVisitor;

public class Num extends AbstractExp {
    public final Number number;

    public Num(Number number, Span span) {
        super(span);
        this.number = number;
    }

    public Num(Token num) {
        super(num.getSpan());
        this.number = num.getIntValue();
    }

    @Override
    public <T> T accept(ExpVisitor<T> prettyPrinter) {
        return prettyPrinter.visit(this);
    }

    @Override
    public <T> void traverse(ExpVisitor<T> visitor) {
        visitor.visit(this);
    }
}
