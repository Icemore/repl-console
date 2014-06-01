package spbau.oop.expr.repl;

import spbau.oop.expr.exp.Exp;
import spbau.oop.expr.parsing.ExpError;
import spbau.oop.expr.visit.PrettyPrinter;
import spbau.oop.expr.visit.VisitorFailException;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;

public class REPLConsole {

    private JFrame frame;
    private UndoManager undo = new UndoManager();
    private Program program;

    public static final String SIMPLIFY = "Simplify";
    public static final String EVALUATE = "Evaluate";

    public static final String GREETING = System.lineSeparator() + ">";

    public void init() throws BadLocationException {
        program = new Program();

        frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // ComboBox setup
        final JComboBox<String> optionPane = new JComboBox<>();
        optionPane.addItem(SIMPLIFY);
        optionPane.addItem(EVALUATE);
        optionPane.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                program.setEvalVariablesFlag(EVALUATE.equals(optionPane.getSelectedItem()));
            }
        });
        frame.add(optionPane, "North");

        // Document setup
        Highlighter highlighter = new UnderlineHighlighter(Color.red);
        Highlighter.HighlightPainter painter = new UnderlineHighlighter.UnderlineHighlightPainter(Color.red);
        StyledDocument document = new REPLDocument(this, highlighter, painter, program);
        ((AbstractDocument) document).setDocumentFilter(new REPLFilter());

        document.addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                if (e.getEdit().getPresentationName().equals("style change")) return;
                undo.addEdit(e.getEdit());
            }
        });

        // TextArea setup
        JTextPane textArea = new JTextPane();
        textArea.setDocument(document);
        textArea.setEditable(true);
        textArea.setHighlighter(highlighter);

        // Adding scroll and placing textArea
        JScrollPane areaScrollPane = new JScrollPane(textArea);
        areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(areaScrollPane, "Center");

        // Key mappings
        textArea.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke("ENTER"), new EvalAction());

        textArea.getKeymap().addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undo.canUndo())
                    undo.undo();
            }
        });

        textArea.getKeymap().
                addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.SHIFT_MASK | InputEvent.CTRL_MASK), new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        program.undo();

                        try {
                            if (program.getResult() == null) return;

                            PrettyPrinter printer = new PrettyPrinter();
                            String res = program.getResult().accept(printer);
                            print(e, res);
                        } catch (VisitorFailException | BadLocationException e1) {
                            e1.printStackTrace();
                        }
                    }
                });

        document.insertString(0, "Welcome to REPL Console!" + GREETING, null);

        frame.setVisible(true);
        frame.setSize(500, 300);
    }

    public String getUserInput(Document document) throws BadLocationException {
        String text = document.getText(0, document.getLength());
        return text.substring(lastLineIndex(document) + GREETING.length());
    }

    public static int lastLineIndex(Document document) throws BadLocationException {
        return document.getText(0, document.getLength()).lastIndexOf(System.lineSeparator());
    }

    private class EvalAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextPane source = (JTextPane) e.getSource();
            Document document = source.getDocument();
            try {
                String userInput = getUserInput(document);

                Exp result = program.evalLine(userInput);
                String resString = "";

                if (program.hasErrors()) {
                    for (ExpError error : program.getErrors()) {
                        resString += error.toString() + System.lineSeparator();
                    }
                } else {
                    PrettyPrinter printer = new PrettyPrinter();
                    resString = result.accept(printer);
                }

                print(e, resString);
            } catch (BadLocationException | VisitorFailException e1) {
                e1.printStackTrace();
            }
        }

    }

    private void print(ActionEvent e, String line) throws BadLocationException {
        JTextPane source = (JTextPane) e.getSource();
        Document document = source.getDocument();

        document.insertString(endOffset(document), System.lineSeparator() + line + GREETING, null);
        source.setCaretPosition(endOffset(document));
        undo.discardAllEdits();
    }

    private int endOffset(Document document) {
        return document.getEndPosition().getOffset() - 1;
    }
}