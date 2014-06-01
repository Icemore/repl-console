package spbau.oop.expr.parsing;

import spbau.oop.expr.exp.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private int currentPos;
    private List<Token> tokens;
    private List<ExpError> errors = new ArrayList<>();

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<ExpError> getErrors() {
        return errors;
    }

    public Exp parse(List<Token> tokens) {
        currentPos = 0;
        this.tokens = tokens;
        errors.clear();


        Exp res = parseRoot();

        if (currentPos != tokens.size()) {
            errors.add(new ExpError(new Span(tokens.get(currentPos).getSpan().getStart(),
                    tokens.get(tokens.size() - 1).getSpan().getEnd()),
                    "unexpected expression"));
        }

        return res;
    }


    private Exp parseRoot() {
        if (tokens.size() < 2 ||
                tokens.get(0).getType() != Token.Type.IDENTIFIER ||
                tokens.get(1).getType() != Token.Type.EQUAL) {
            return parseArithmeticExpression();
        } else {
            return parseAssignment();
        }
    }

    private Exp parseAssignment() {
        Variable left = new Variable(tokens.get(0));
        currentPos += 2;
        Exp right = parseArithmeticExpression();

        return new Assignment(left, right);
    }

    private Exp parseArithmeticExpression() {
        Exp res = parseMulExpression();

        while (currentPos < tokens.size()) {
            Token next = tokens.get(currentPos);
            if (next.getType() != Token.Type.PLUS && next.getType() != Token.Type.MINUS) break;

            currentPos++;
            Exp right = parseMulExpression();

            if (next.getType() == Token.Type.PLUS) {
                res = new Sum(res, right);
            } else {
                res = new Sub(res, right);
            }
        }

        return res;
    }

    private Exp parseMulExpression() {
        Exp res = parseBasicExpression();

        while (currentPos < tokens.size()) {
            Token next = tokens.get(currentPos);
            if (next.getType() != Token.Type.MUL && next.getType() != Token.Type.DIV) break;

            currentPos++;
            Exp right = parseBasicExpression();

            if (next.getType() == Token.Type.MUL) {
                res = new Mul(res, right);
            } else {
                res = new Div(res, right);
            }
        }

        return res;
    }

    private Exp parseBasicExpression() {
        if (currentPos >= tokens.size()) {
            Span lastTokenSpan = tokens.get(tokens.size() - 1).getSpan();
            return new Missing(new Span(lastTokenSpan.getEnd() - 1, lastTokenSpan.getEnd()));
        }

        Token cur = tokens.get(currentPos);

        switch (cur.getType()) {
            case IDENTIFIER:
                currentPos++;
                return new Variable(cur);
            case NUMBER:
                currentPos++;
                return new Num(cur);
            case OPEN_PAREN:
                currentPos++;
                Exp res = parseArithmeticExpression();

                //check for close_parenthesis
                if (currentPos >= tokens.size() || tokens.get(currentPos).getType() != Token.Type.CLOSE_PAREN) {
                    errors.add(new ExpError(getAfterSpan(res), "missing close parenthesis"));
                } else {
                    currentPos++;
                }

                return res;
            default:
                return new Missing(cur.getSpan());
        }
    }

    private Span getAfterSpan(Exp exp) {
        int start = exp.getSpan().getEnd() - 1;
        int end;

        if (currentPos < tokens.size()) {
            end = tokens.get(currentPos).getSpan().getStart();
        } else {
            end = tokens.get(tokens.size() - 1).getSpan().getEnd() + 1;
        }

        return new Span(start, end);
    }
}
