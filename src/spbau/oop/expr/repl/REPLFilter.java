package spbau.oop.expr.repl;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

class REPLFilter extends DocumentFilter {
    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (cursorOnLastLine(offset, fb)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
        if (offset > REPLConsole.lastLineIndex(fb.getDocument()) + 2) {
            super.remove(fb, offset, length);
        }
    }

    public void replace(final FilterBypass fb, final int offset, final int length, final String text, final AttributeSet attrs)
            throws BadLocationException {
        if (cursorOnLastLine(offset, fb)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private static boolean cursorOnLastLine(int offset, FilterBypass fb) {
        return cursorOnLastLine(offset, fb.getDocument());
    }

    private static boolean cursorOnLastLine(int offset, Document document) {
        int lastLineIndex = 0;
        try {
            lastLineIndex = REPLConsole.lastLineIndex(document);
        } catch (BadLocationException e) {
            return false;
        }
        return offset > lastLineIndex;
    }
}
