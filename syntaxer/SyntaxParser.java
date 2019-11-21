import entities.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SyntaxParser {
    private String mInputPath;
    private List<RawToken> mTokens;
    private int mTokenPosition;

    public SyntaxParser(String inputPath) {
        this.mInputPath = inputPath;
        this.mTokens = new ArrayList<>();
    }

    public Program parse() throws IOException {
        /* Call lexer to get tokens */
        prepareTokens();

        Program program = new Program();
        Statement statement;

        while ((statement = parseStatement()) != null) {
            program.addStatement(statement);
        }

        return program;
    }

    private void prepareTokens() throws IOException {
        LexicalAnalysis lexer = new LexicalAnalysis();
        mTokens = lexer.lexerGetTokens(mInputPath, ".temp");
        mTokenPosition = -1;
    }

    private String getToken(int position) {
        return mTokens.get(position).val;
    }

    private RawToken getRawToken(int position) {
        return mTokens.get(position);
    }

    private String getNextToken() {
        return getToken(mTokenPosition++);
    }

    private RawToken getNextRawToken() {
        return getRawToken(mTokenPosition++);
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
        if (getNextToken().equals("if")) return null;
        Expression condition = parseExpression();

        /* Check for `then` */
        if (!getNextToken().equals("then")) return null;

        Body body = parseBody();

        /* Check for `else` */
        Body elseBody = null;
        if (getNextToken().equals("else")) {
            elseBody = parseBody();
        } else revertTokenPosition();

        /* Check for end */
        if (!getNextToken().equals("end")) return null;

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
        /* Return : return [ Expression ] */
        Expression expression = parseExpression();
        if (expression == null) {
            return null;
        } else {
            return new Return(expression);
        }
    }

    private Print parsePrint() {
        /* Print : print Expression { ',' Expression } */
        Expression expression = parseExpression();
        if (expression == null) {
            return null;
        } else {
            Print result = new Print(expression);
            while (getNextToken().equals(",")) {
                expression = parseExpression();
                if (expression == null) {
                    break;
                } else {
                    result.addExpression(expression);
                }
            }
            revertTokenPosition();
            return result;
        }
    }

    private VariableDefinition parseVariableDefinition() {
        /* VariableDefinition : IDENT [ ':=' Expression ] */
        RawToken token = getNextRawToken();
        if (token.isIdentifier()) {
            Identifier identifier = new Identifier(token.val);
            VariableDefinition result = new VariableDefinition(identifier);
            if (getNextToken().equals(":=")) {
                Expression expression = parseExpression();
                result.setExpression(expression);
            } else {
                revertTokenPosition();
            }
            return result;
        } else {
            revertTokenPosition();
            return null;
        }
    }

    private Reference parseReference() {
        // TODO: parse reference
        return null;
    }

    private Expression parseExpression() {
        Relation relation = parseRelation();
        if(relation == null) {
            return null;
        } else {
            Expression result = new Expression(relation);
            String operator = getNextToken();
            while(operator.equals("or") || operator.equals("and") || operator.equals("xor")) {
                relation = parseRelation();
                switch (operator) {
                    case "xor": result.addRelation(relation, LogicalOperator.XOR);
                    case "and": result.addRelation(relation, LogicalOperator.AND);
                    case "or": result.addRelation(relation, LogicalOperator.OR);
                    operator = getNextToken();
                }
            }
            revertTokenPosition();
            return result;
        }
    }

    private Relation parseRelation() {
        /* Relation: Factor [ ( '<'|'<='|'>'|'>='|'='|'/=' Factor ] ) */
        Factor firstFactor = parseFactor();
        if(firstFactor == null) {
            return null;
        } else {
            String operator = getNextToken();
            Factor secondFactor = parseFactor();
            if (isRelationOperator(operator)) {
                switch (operator) {
                    case "<":  return new Relation(firstFactor, RelationOperator.LESS, secondFactor);
                    case "<=": return new Relation(firstFactor, RelationOperator.LESS_EQ, secondFactor);
                    case ">":  return new Relation(firstFactor, RelationOperator.GREATER, secondFactor);
                    case ">=": return new Relation(firstFactor, RelationOperator.GREATER_EQ, secondFactor);
                    case "=":  return new Relation(firstFactor, RelationOperator.EQUAL, secondFactor);
                    case "/=": return new Relation(firstFactor, RelationOperator.NOT_EQUAL, secondFactor);
                }
            }
            revertTokenPosition();
            return new Relation(firstFactor);
        }
    }

    private Factor parseFactor() {
        /* Factor : Term { [ '+' | '-' ] Term } */
        Term term = parseTerm();
        if (term == null) {
            return null;
        } else {
            Factor result = new Factor(term);
            String operator = getNextToken();
            while (isFactorSign(operator)) {
                term = parseTerm();
                switch (operator) {
                    case "+": result.addTerm(term, ArithmeticOperator.ADD);
                    case "-": result.addTerm(term, ArithmeticOperator.SUB);
                }
                operator = getNextToken();
            }
            revertTokenPosition();
            return result;
        }
    }

    private Term parseTerm() {
        /* Term : Unary { ( '*' | '/' ) Unary } */
        Unary unary = parseUnary();
        if (unary == null) {
            return null;
        } else {
            Term result = new Term(unary);
            String operator = getNextToken();
            while (isTermSign(operator)) {
                unary = parseUnary();
                switch (operator) {
                    case "*": result.addUnary(unary, MultiplicationOperator.MUL);
                    case "/": result.addUnary(unary, MultiplicationOperator.DIV);
                }
                operator = getNextToken();
            }
            revertTokenPosition();
            return result;
        }
    }

    private Unary parseUnary() {
        RawToken token = getNextRawToken();
        if (isPrimarySign(token.val)) {
            Primary primary = parsePrimary();
            switch (token.val) {
                case "+": return new SignedPrimary(primary, UnarySign.ADD);
                case "-": return new SignedPrimary(primary, UnarySign.SUB);
                case "not": return new SignedPrimary(primary, UnarySign.NOT);
            }
        } else if (token.val.equals("(")) {
            Expression expression = parseExpression();
            return new IsolatedExpression(expression);
        } else {
            revertTokenPosition();
            Primary primary = parsePrimary();
            String nextToken = getNextToken();
            if (nextToken.equals("is")) {
                nextToken = getNextToken();
                switch (nextToken) {
                    case "int": return new Is(primary, TypeIndicator.INT);
                    case "real": return new Is(primary, TypeIndicator.REAL);
                    case "bool": return new Is(primary, TypeIndicator.BOOL);
                    case "string": return new Is(primary, TypeIndicator.STRING);
                    case "empty": return new Is(primary, TypeIndicator.EMPTY);
                    case "[":
                        nextToken = getNextToken();
                        if (nextToken.equals("]")) {
                            return new Is(primary, TypeIndicator.VECTOR);
                        } else {
                            return null;
                        }
                    case "{":
                        nextToken = getNextToken();
                        if (nextToken.equals("}")) {
                            return new Is(primary, TypeIndicator.TUPLE);
                        } else {
                            return null;
                        }
                    case "func": return new Is(primary, TypeIndicator.FUNC);
                }
            }
        }
        return null;
    }

    private Primary parsePrimary() {
        RawToken token = getNextRawToken();
        if (token.type == RawToken.TokenType.IDENTIFIER) {
            revertTokenPosition();
            return parseReference();
        } else if (token.type == RawToken.TokenType.LITERAL) {
            revertTokenPosition();
            return parseLiteral();
        } else if (token.type == RawToken.TokenType.KEYWORD && token.val.equals("func")) {
            return parseFunctionalLiteral();
        }
        return null;
    }

    private FunctionalLiteral parseFunctionalLiteral() {
        return null;
    }

    private Literal parseLiteral() {
        return null;
    }

    private Body parseBody() {
        /* Body : { Statement } */
        Statement statement = parseStatement();
        if (statement == null) {
            return null;
        } else {
            Body result = new Body(statement);
            while (true) {
                statement = parseStatement();
                if(statement != null) {
                    result.addStatement(statement);
                } else {
                    return result;
                    // #TODO: handle syntax error
                }
            }
        }
    }

    private static boolean isRelationOperator(String operator) {
        return operator.equals("<") || operator.equals("<=") ||
                operator.equals(">") || operator.equals(">=") ||
                operator.equals("=") || operator.equals("/=");
    }

    private boolean isTermSign(String operator) {
        return operator.equals("*") || operator.equals("/");
    }

    private boolean isFactorSign(String operator) {
        return operator.equals("+") || operator.equals("-");
    }

    private boolean isPrimarySign(String operator) {
        return operator.equals("+") || operator.equals("-") || operator.equals("not");
    }

}