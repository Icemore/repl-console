package spbau.oop.expr.parsing;

public class ExpError {
    private Span span;
    private String reason;

    public Span getSpan() {
        return span;
    }

    public String getReason() {
        return reason;
    }

    public ExpError(Span span, String reason) {

        this.span = span;
        this.reason = reason;
    }

    @Override
    public String toString() {
        return String.format("Error (%d, %d): %s", span.getStart(), span.getEnd(), reason);
    }

    static ExpError getLexicalError(Token token) {
        return new ExpError(token.getSpan(), String.format("unexpected symbol \'%s\'", token.getValue()));
    }
}
