package az.ada.plt.project.langcraft.utils;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private String input;
    private int position;

    public Lexer(String input) {
        this.input = input;
        this.position = 0;
    }

    public List<Token> tokenize() throws LexerException {
        List<Token> tokens = new ArrayList<>();

        while (position < input.length()) {
            char currentChar = input.charAt(position);

            if (Character.isWhitespace(currentChar)) {
                position++;
            } else if (currentChar == '(') {
                tokens.add(new Token(TokenType.LEFT_PAREN, "(", position));
                position++;
            } else if (currentChar == ')') {
                tokens.add(new Token(TokenType.RIGHT_PAREN, ")", position));
                position++;
            } else if (currentChar == '{') {
                tokens.add(new Token(TokenType.LEFT_CURLY_BRACKET, "{", position));
                position++;
            } else if (currentChar == '}') {
                tokens.add(new Token(TokenType.RIGHT_CURLY_BRACKET, "}", position));
                position++;
            } else if (currentChar == '=') {
                tokens.add(new Token(TokenType.ASSIGN, "=", position));
                position++;
            } else if (currentChar == ';') {
                tokens.add(new Token(TokenType.SEMICOLON, ";", position));
                position++;
            } else if (Character.isLetter(currentChar)) {
                int start = position;
                while (position < input.length() && Character.isLetterOrDigit(input.charAt(position))) {
                    position++;
                }
                String identifier = input.substring(start, position);
                tokens.add(new Token(TokenType.IDENTIFIER, identifier, start));
            } else if (Character.isDigit(currentChar)) {
                int start = position;
                while (position < input.length() && Character.isDigit(input.charAt(position))) {
                    position++;
                }
                String number = input.substring(start, position);
                tokens.add(new Token(TokenType.NUMBER, number, start));
            } else {
                throw new LexerException("Unexpected character: " + currentChar + " at position " + position);
            }
        }

        return tokens;
    }
}
