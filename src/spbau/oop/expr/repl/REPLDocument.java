package spbau.oop.expr.repl;

import spbau.oop.expr.parsing.ExpError;
import spbau.oop.expr.parsing.Span;
import spbau.oop.expr.parsing.Token;
import spbau.oop.expr.visit.VisitorFailException;

import javax.swing.text.*;
import java.awt.*;

class REPLDocument extends DefaultStyledDocument {
    private REPLConsole replConsole;
    private Style var, num, def;
    private Highlighter highlighter;
    private Highlighter.HighlightPainter painter;
    private Program program;

    public REPLDocument(REPLConsole replConsole, Highlighter highlighter, Highlighter.HighlightPainter painter, Program program) {
        super();
        this.replConsole = replConsole;
        this.highlighter = highlighter;
        this.painter = painter;
        this.program = program;

        def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        def = this.addStyle("def", def);

        var = this.addStyle("var", def);
        StyleConstants.setForeground(var, Color.BLUE);

        num = this.addStyle("num", def);
        StyleConstants.setForeground(num, Color.ORANGE);
    }

    @Override
    protected void insertUpdate(DefaultDocumentEvent chng, AttributeSet attr) {
        super.insertUpdate(chng, attr);
        handleColoring();
    }

    @Override
    protected void removeUpdate(DefaultDocumentEvent chng) {
        super.removeUpdate(chng);
        handleColoring();
    }

    private void handleColoring() {
        try {
            String userInput = replConsole.getUserInput(this);
            int lineStartIndex = REPLConsole.lastLineIndex(this) + REPLConsole.GREETING.length();

            // clear coloring
            highlighter.removeAllHighlights();
            colorRange(0, userInput.length(), def, lineStartIndex);

            program.parseLine(userInput);
            colorSyntax(program.getTokens(), lineStartIndex);
            highlightErrors(program.getErrors(), lineStartIndex);

        } catch (BadLocationException | VisitorFailException e) {
            e.printStackTrace();
        }
    }

    private void colorSyntax(java.util.List<Token> tokens, int offset) throws BadLocationException {
        for (Token cur : tokens) {
            switch (cur.getType()) {
                case NUMBER:
                    colorRange(cur.getSpan(), num, offset);
                    break;
                case IDENTIFIER:
                    colorRange(cur.getSpan(), var, offset);
                    break;
            }
        }
    }

    private void highlightErrors(java.util.List<ExpError> errors, int offset) throws BadLocationException {
        for (ExpError error : errors) {
            highlightRange(error.getSpan(), offset);
        }
    }

    private void colorRange(int start, int end, Style style, int offset) {
        this.setCharacterAttributes(offset + start, end - start, style, true);
    }

    private void colorRange(Span span, Style style, int offset) {
        colorRange(span.getStart(), span.getEnd(), style, offset);
    }

    private void highlightRange(Span span, int offset) throws BadLocationException {
        highlighter.addHighlight(offset + span.getStart(), offset + span.getEnd(), painter);
    }
}
