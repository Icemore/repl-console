package spbau.oop.expr;

import spbau.oop.expr.repl.REPLConsole;

import javax.swing.text.BadLocationException;

public class Main {
    public static void main(String[] args) {
        REPLConsole replConsole = new REPLConsole();
        try {
            replConsole.init();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
