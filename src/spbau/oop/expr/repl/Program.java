package spbau.oop.expr.repl;

import spbau.oop.expr.exp.Exp;
import spbau.oop.expr.parsing.ExpError;
import spbau.oop.expr.parsing.Lexer;
import spbau.oop.expr.parsing.Parser;
import spbau.oop.expr.parsing.Token;
import spbau.oop.expr.visit.ErrorCheckVisitor;
import spbau.oop.expr.visit.VisitorFailException;

import java.util.*;

public class Program {
    private Map<String, Number> context = new HashMap<>();
    private Lexer lexer = new Lexer();
    private Parser parser = new Parser();
    private boolean evalVariables;
    private Deque<EvalAction> history = new ArrayDeque<>();

    private List<Token> tokens;
    private List<ExpError> errors;

    public Exp evalLine(String userInput) throws VisitorFailException {
        Exp result = parseLine(userInput);

        if (!errors.isEmpty()) return result;

        history.addLast(new EvalAction(result, evalVariables, context));

        return history.getLast().doAction();
    }

    public Exp parseLine(String userInput) throws VisitorFailException {
        tokens = lexer.parseString(userInput);

        if (lexer.hasErrors() || tokens.isEmpty()) {
            errors = lexer.getErrors();
            return null;
        }

        Exp result = parser.parse(tokens);
        errors = parser.getErrors();

        ErrorCheckVisitor checker = new ErrorCheckVisitor(evalVariables, context, errors);
        result.accept(checker);

        return result;
    }

    public void undo() {
        if (!history.isEmpty()) {
            EvalAction action = history.pollLast();
            action.undo();
        }
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public List<ExpError> getErrors() {
        return errors;
    }

    public Exp getResult() throws VisitorFailException {
        if (history.isEmpty()) return null;
        else return history.getLast().doAction();
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void setEvalVariablesFlag(boolean evalVariables) {
        this.evalVariables = evalVariables;
    }
}
