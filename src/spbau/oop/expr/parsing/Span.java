package spbau.oop.expr.parsing;

public class Span {
    private int start;
    private int end;

    public int getEnd() {
        return end;
    }

    public int getStart() {
        return start;
    }

    public Span(int start, int end) {
        this.start = start;
        this.end = end;
    }
}
