package spbau.oop.expr.exp;

import spbau.oop.expr.parsing.Span;
import spbau.oop.expr.parsing.Token;
import spbau.oop.expr.visit.ExpVisitor;
import spbau.oop.expr.visit.VisitorFailException;

public class Variable extends AbstractExp {
    public final String name;

    public Variable(String name, Span span) {
        super(span);
        this.name = name;
    }

    public Variable(Token id) {
        super(id.getSpan());
        this.name = id.getValue();
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
