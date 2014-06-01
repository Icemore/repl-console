package spbau.oop.expr.parsing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Lexer {
    private List<ExpError> errors = new ArrayList<>();

    public List<Token> parseString(String string) {
        TokenIterator it = new TokenIterator(string);
        errors.clear();

        List<Token> result = new ArrayList<>();
        while (it.hasNext()) {
            Token cur = it.next();
            result.add(cur);

            if (cur.getType() == Token.Type.ERROR) {
                errors.add(ExpError.getLexicalError(cur));
            }
        }

        return result;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<ExpError> getErrors() {
        return errors;
    }

    private class TokenIterator implements Iterator<Token> {
        private int currentPos;
        private String string;

        public TokenIterator(String string) {
            this.string = string;
            currentPos = 0;
        }

        @Override
        public boolean hasNext() {
            skipBlanks();
            return currentPos < string.length();
        }

        @Override
        public Token next() {
            skipBlanks();

            if (currentPos == string.length()) {
                return new Token(Token.Type.EOL, string.length());
            }

            Token result;

            if (Character.isDigit(string.charAt(currentPos))) {
                result = parseNumber();
            } else if (Character.isLetter(string.charAt(currentPos))) {
                result = parseIdentifier();
            } else {
                switch (string.charAt(currentPos)) {
                    case '+':
                        result = new Token(Token.Type.PLUS, currentPos);
                        break;
                    case '-':
                        result = new Token(Token.Type.MINUS, currentPos);
                        break;
                    case '*':
                        result = new Token(Token.Type.MUL, currentPos);
                        break;
                    case '/':
                        result = new Token(Token.Type.DIV, currentPos);
                        break;
                    case '=':
                        result = new Token(Token.Type.EQUAL, currentPos);
                        break;
                    case '(':
                        result = new Token(Token.Type.OPEN_PAREN, currentPos);
                        break;
                    case ')':
                        result = new Token(Token.Type.CLOSE_PAREN, currentPos);
                        break;
                    default:
                        result = new Token(Token.Type.ERROR, currentPos, string.substring(currentPos, currentPos + 1));
                }

                currentPos++;
            }

            return result;
        }

        @Override
        public void remove() {
        }

        private void skipBlanks() {
            while (currentPos < string.length() &&
                    Character.isSpaceChar(string.charAt(currentPos))) {
                currentPos++;
            }
        }

        private Token parseIdentifier() {
            int start = currentPos;

            while (currentPos < string.length() &&
                    Character.isLetterOrDigit(string.charAt(currentPos))) {
                currentPos++;
            }

            int end = currentPos;

            return new Token(Token.Type.IDENTIFIER, new Span(start, end), string.substring(start, end));
        }

        private Token parseNumber() {
            int start = currentPos;

            while (currentPos < string.length() &&
                    Character.isDigit(string.charAt(currentPos))) {
                currentPos++;
            }

            int end = currentPos;

            return new Token(Token.Type.NUMBER, new Span(start, end), string.substring(start, end));
        }
    }
}
