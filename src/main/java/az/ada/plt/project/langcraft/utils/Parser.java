az.ada.plt.project.langcraft.utils;

import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int position;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
    }

    public void parse() throws ParserException {
        try {
            program();
        } catch (IndexOutOfBoundsException e) {
            throw new ParserException("Unexpected end of input");
        }
    }

    private void program() throws ParserException {
        function();
        match(TokenType.SEMICOLON);
        while (position < tokens.size()) {
            function();
            match(TokenType.SEMICOLON);
        }
    }

    private void function() throws ParserException {
        match(TokenType.TYPEINT);
        match(TokenType.IDENTIFIER);
        match(TokenType.LEFT_PAREN);
        match(TokenType.RIGHT_PAREN);
        compoundStatement();
    }

    private void compoundStatement() throws ParserException {
        match(TokenType.LEFT_CURLY_BRACKET);
        while (position < tokens.size() && tokens.get(position).getType() != TokenType.RIGHT_CURLY_BRACKET) {
            statement();
        }
        match(TokenType.RIGHT_CURLY_BRACKET);
    }

    private void statement() throws ParserException {
        if (tokens.get(position).getType() == TokenType.TYPEINT) {
            declaration();
        } else if (tokens.get(position).getType() == TokenType.IDENTIFIER) {
            assignment();
        } else if (tokens.get(position).getType() == TokenType.IF) {
            ifStatement();
        } else if (tokens.get(position).getType() == TokenType.RETURN) {
            returnStatement();
        } else {
            throw new ParserException("Unexpected token: " + tokens.get(position));
        }
    }

    private void declaration() throws ParserException {
        match(TokenType.TYPEINT);
        match(TokenType.IDENTIFIER);
        if (tokens.get(position).getType() == TokenType.ASSIGN) {
            match(TokenType.ASSIGN);
            expression();
        }
        match(TokenType.SEMICOLON);
    }

    private void assignment() throws ParserException {
        match(TokenType.IDENTIFIER);
        match(TokenType.ASSIGN);
        expression();
        match(TokenType.SEMICOLON);
    }

    private void ifStatement() throws ParserException {
        match(TokenType.IF);
        match(TokenType.LEFT_PAREN);
        expression();
        match(TokenType.RIGHT_PAREN);
        compoundStatement();
        if (tokens.get(position).getType() == TokenType.ELSE) {
            match(TokenType.ELSE);
            compoundStatement();
        }
    }

    private void returnStatement() throws ParserException {
        match(TokenType.RETURN);
        expression();
        match(TokenType.SEMICOLON);
    }

    private void expression() throws ParserException {
        additiveExpression();
    }

    private void additiveExpression() throws ParserException {
        multiplicativeExpression();
        while (position < tokens.size() &&
                (tokens.get(position).getType() == TokenType.PLUS || tokens.get(position).getType() == TokenType.MINUS)) {
            if (tokens.get(position).getType() == TokenType.PLUS) {
                match(TokenType.PLUS);
            } else {
                match(TokenType.MINUS);
            }
            multiplicativeExpression();
        }
    }

    private void multiplicativeExpression() throws ParserException {
        primaryExpression();
        while (position < tokens.size() &&
                (tokens.get(position).getType() == TokenType.MULTIPLY || tokens.get(position).getType() == TokenType.DIVIDE)) {
            if (tokens.get(position).getType() == TokenType.MULTIPLY) {
                match(TokenType.MULTIPLY);
            } else {
                match(TokenType.DIVIDE);
            }
            primaryExpression();
        }
    }

    private void primaryExpression() throws ParserException {
        if (tokens.get(position).getType() == TokenType.IDENTIFIER || tokens.get(position).getType() == TokenType.NUMBER) {
            match(tokens.get(position).getType());
        } else if (tokens.get(position).getType() == TokenType.LEFT_PAREN) {
            match(TokenType.LEFT_PAREN);
            expression();
            match(TokenType.RIGHT_PAREN);
        } else {
            throw new ParserException("Unexpected token: " + tokens.get(position));
        }
    }

    private void match(TokenType expectedTokenType) throws ParserException {
        Token currentToken = tokens.get(position);
        if (currentToken.getType() == expectedTokenType) {
            position++;
        } else {
            throw new ParserException("Expected token: " + expectedTokenType +
                    ", but found: " + currentToken.getType() + " at position " + currentToken.getPosition());
        }
    }
}
