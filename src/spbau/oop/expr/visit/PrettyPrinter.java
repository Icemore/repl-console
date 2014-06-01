package spbau.oop.expr.visit;

import spbau.oop.expr.exp.*;

public class PrettyPrinter implements ExpVisitor<String> {
    public String visit(Num exp) {
        return exp.number.toString();
    }

    public String visit(Div exp) throws VisitorFailException {
        return printBinaryExpr(exp, '/');
    }

    @Override
    public String visit(Variable var) {
        return var.name;
    }

    @Override
    public String visit(Assignment ass) throws VisitorFailException {
        String res = "";
        res += ass.left.accept(this);
        res += " = ";
        res += ass.right.accept(this);
        return res;
    }

    @Override
    public String visit(Missing miss) throws VisitorFailException {
        return " MISSING ";
    }

    @Override
    public String visit(Sub sub) throws VisitorFailException {
        return printBinaryExpr(sub, '-');
    }

    public String visit(Mul exp) throws VisitorFailException {
        return printBinaryExpr(exp, '*');
    }

    public String visit(Sum exp) throws VisitorFailException {
        return printBinaryExpr(exp, '+');
    }

    private String printBinaryExpr(BiExp exp, char op) throws VisitorFailException {
        String res = "";
        res += "(";
        res += exp.left.accept(this);
        res += " " + op + " ";
        res += exp.right.accept(this);
        res += ")";
        return res;
    }
}
