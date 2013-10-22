package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;

import java.util.List;

public class CallExprToken extends ExprToken {
    private Token name;
    private List<ExprStmtToken> parameters;

    public CallExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    public List<ExprStmtToken> getParameters() {
        return parameters;
    }

    public void setParameters(List<ExprStmtToken> parameters) {
        this.parameters = parameters;
    }

    public void setName(Token name) {
        this.name = name;
    }

    public Token getName() {
        return name;
    }
}
