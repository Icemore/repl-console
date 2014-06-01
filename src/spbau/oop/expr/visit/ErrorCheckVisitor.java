package spbau.oop.expr.visit;

import spbau.oop.expr.exp.*;
import spbau.oop.expr.parsing.ExpError;

import java.util.List;
import java.util.Map;

public class ErrorCheckVisitor implements ExpVisitor<Void> {
    private List<ExpError> errors;
    private boolean checkVariables;
    private Map<String, Number> context;

    public ErrorCheckVisitor(boolean checkVariables, Map<String, Number> context, List<ExpError> errors) {
        this.checkVariables = checkVariables;
        this.context = context;
        this.errors = errors;
    }

    public void setCheckVariablesFlag(boolean checkVariables) {
        this.checkVariables = checkVariables;
    }

    public List<ExpError> getErrors() {
        return errors;
    }

    @Override
    public Void visit(Num num) {
        return null;
    }

    @Override
    public Void visit(Sum sum) throws VisitorFailException {
        return visitBinaryExp(sum);
    }

    @Override
    public Void visit(Mul mul) throws VisitorFailException {
        return visitBinaryExp(mul);
    }

    @Override
    public Void visit(Div div) throws VisitorFailException {
        visitBinaryExp(div);

        if(div.right instanceof Num && ((Num) div.right).number.intValue() == 0) {
            errors.add(new ExpError(div.getSpan(), "division by 0"));
        }

        return null;
    }

    @Override
    public Void visit(Variable var) throws VisitorFailException {
        if (checkVariables) {
            if (!context.containsKey(var.name)) {
                errors.add(new ExpError(var.getSpan(), "undefined variable " + var.name));
            }
        }
        return null;
    }

    @Override
    public Void visit(Assignment ass) throws VisitorFailException {
        return ass.right.accept(this);
    }

    @Override
    public Void visit(Missing miss) throws VisitorFailException {
        errors.add(new ExpError(miss.getSpan(), "missing operand"));
        return null;
    }

    @Override
    public Void visit(Sub sub) throws VisitorFailException {
        return visitBinaryExp(sub);
    }

    public Void visitBinaryExp(BiExp exp) throws VisitorFailException {
        exp.left.accept(this);
        exp.right.accept(this);
        return null;
    }
}
