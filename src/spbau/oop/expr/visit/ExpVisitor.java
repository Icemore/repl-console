package spbau.oop.expr.visit;

import spbau.oop.expr.exp.*;

public interface ExpVisitor<T> {
    T visit(Num num);

    T visit(Sum sum) throws VisitorFailException;

    T visit(Mul mul) throws VisitorFailException;

    T visit(Div div) throws VisitorFailException;

    T visit(Variable var) throws VisitorFailException;

    T visit(Assignment ass) throws VisitorFailException;

    T visit(Missing miss) throws VisitorFailException;

    T visit(Sub sub) throws VisitorFailException;
}
