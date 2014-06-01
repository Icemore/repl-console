package spbau.oop.expr.repl;

import spbau.oop.expr.exp.Assignment;
import spbau.oop.expr.exp.Exp;
import spbau.oop.expr.exp.Num;
import spbau.oop.expr.exp.Variable;
import spbau.oop.expr.visit.Evaluator;
import spbau.oop.expr.visit.VisitorFailException;

import java.util.Map;

public class EvalAction {
    private Exp exp;
    private boolean evalVariables;
    private boolean done;

    private Map<String, Number> context;

    private String name;
    private Number previous;

    public EvalAction(Exp exp, boolean evalVariables, Map<String, Number> context) {
        this.exp = exp;
        this.evalVariables = evalVariables;
        this.context = context;
        done = false;
        name = null;
    }

    public Exp doAction() throws VisitorFailException {
        if (done) return exp;

        Evaluator evaluator = new Evaluator(context, evalVariables);
        exp = exp.accept(evaluator);


        if (evalVariables && exp instanceof Assignment) {
            Assignment assignment = ((Assignment) exp);

            name = ((Variable) assignment.left).name;
            previous = context.get(name);

            context.put(name, ((Num) assignment.right).number);

            exp = assignment.right;
        }

        done = true;
        return exp;
    }

    public void undo() {
        if (!done || name == null) return;

        if (previous == null) context.remove(name);
        else context.put(name, previous);
    }
}
