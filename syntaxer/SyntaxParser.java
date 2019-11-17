import entities.*;

public class SyntaxParser {
    private String mInputPath;
    private List<RawToken> mTokens;
    private int mTokenPosition;

    public SyntaxParser(String inputPath) {
        this.mInputPath = inputPath;
    }

    public Program parse() {
        /* Call lexer to get tokens */
        prepareTokens();

        Program program = new Program();
        Statement statement;

        while ((statement = parseStatement()) != null) {
            program.addStatement(statement);
        }

        return program;
    }

    private void prepareTokens() {
        LexicalAnalysis lexer = new LexicalAnalysis();
        mTokens = lexer.lexerGetTokens(mInputPath, ".temp");
        mTokenPosition = -1;
    }

    private String getToken(int position) {
        return mTokens[position].val;
    }

    private String getNextToken() {
        return getToken(mTokenPosition++);
    }

    private void revertTokenPosition() {
        mTokenPosition--;
    }

    /**
     * Parses statement. Tries all the cases.
     * @return Statement
     */
    private Statement parseStatement() {
        Statement result = parseDeclaration();

        if (result == null) result = parseAssignment();
        if (result == null) result = parseIf();
        if (result == null) result = parseLoop();
        if (result == null) result = parsePrint();
        if (result == null) result = parseReturn();

        return result;
    }

    private Declaration parseDeclaration() {
        /* Declaration starts with `var` */
        if (!getNextToken().equals("var")) return null;

        /* Then — variable definitions */
        Declaration declaration = new Declaration(parseVariableDefinition());
        while (getNextToken().equals(",")) {
            declaration.addVariableDefinition(parseVariableDefinition());
        }

        return declaration;
    }

    private Assignment parseAssignment() {
        Reference ref = parseReference();

        /* Check for next token */
        if (!getNextToken().equals(":=")) return null;

        Expression expr = parseExpression();

        return new Assignment(ref, expr);
    }

    private If parseIf() {
        /* Conditional starts with `if` */
        if (!getNextToken.equals("if")) return null;

        Expression condition = parseExpression();

        /* Check for `then` */
        if (!getNextToken.equals("then")) return null;

        Body body = parseBody();

        /* Check for `else` */
        Body elseBody = null;
        if (getNextToken.equals("else")) {
            elseBody = parseBody();
        } else revertTokenPosition();

        /* Check for end */
        if (!getNextToken.equals("end")) return null;

        return new If(condition, body, elseBody);
    }

    private Loop parseLoop() {
        String token = getNextToken();

        if (token.equals("while")) {
            Expression condition = parseExpression();

            /* Check for `loop` */
            if (!getNextToken().equals("loop")) return null;

            Body body = parseBody();

            /* Check for `end` */
            if (!getNextToken().equals("end")) return null;

            return new WhileLoop(condition, body);
        } else if (token.equals("for")) {
            token = getNextToken();

            /* Check whether the next token is `in`: we have identifier */
            Identifier identifier = null;
            if (getNextToken().equals("in")) {
                identifier = new Identifier(token);
            } else revertTokenPosition();

            /* Check whether next token is not`loop`: we have range */
            Range range = null;
            if (!getNextToken().equals("loop")) {
                Expression rangeStart = parseExpression();

                /* Next token must be `..` */
                if (!getNextToken().equals("..")) return null;

                Expression rangeEnd = parseExpression();
                range = new Range(rangeStart, rangeEnd);
            } else revertTokenPosition();

            /* Next goes `loop` */
            if (!getNextToken().equals("loop")) return null;

            Body body = parseBody();

            /* Next goes `end` */
            if (!getNextToken().equals("end")) return null;

            return new ForLoop(body, range, identifier);
        }

        return null;
    }

    private Return parseReturn() {
        return null;
    }

    private Print parsePrint() {
        return null;
    }

    private VariableDefinition parseVariableDefinition() {
        return null;
    }

    private Reference parseReference() {
        return null;
    }

    private Expression parseExpression() {
        return null;
    }

    private Body parseBody() {
        return null;
    }
}