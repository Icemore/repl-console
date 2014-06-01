package spbau.oop.expr.parsing;

public class Token {
    public enum Type {
        NUMBER, IDENTIFIER, PLUS, MINUS, MUL, DIV, OPEN_PAREN, CLOSE_PAREN, EQUAL, EOL, ERROR
    }

    private String value;
    private Type type;
    private Span span;

    public Token(Type type, Span span, String value) {
        this.value = value;
        this.type = type;
        this.span = span;
    }

    public Token(Type type, Span span) {
        this(type, span, "");
    }

    public Token(Type type, int pos) {
        this(type, pos, "");
    }

    public Token(Type type, int pos, String value) {
        this(type, new Span(pos, pos + 1), value);
    }

    public String getValue() {
        return value;
    }

    public int getIntValue() throws NumberFormatException {
        return Integer.parseInt(value);
    }

    public Span getSpan() {
        return span;
    }

    public Type getType() {
        return type;
    }
}
