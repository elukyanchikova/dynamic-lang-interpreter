package syntaxer;
import syntaxer.entities.*;
import lexer.*;
import lexer.LexicalAnalysis;

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
        if (mTokenPosition >= mTokens.size()) {
            System.out.println("Syntax analyzing finished");
        } else {
            // TODO: Syntax error info showing error line with position
            System.out.println("Syntax error");
        }
        return program;
    }

    private void prepareTokens() throws IOException {
        LexicalAnalysis lexer = new LexicalAnalysis();
        mTokens = lexer.lexerGetTokens(mInputPath, ".temp");
        mTokenPosition = 0;
    }

    private String getToken(int position) {
        if (position >= mTokens.size()) return "EOF";
        return mTokens.get(position).val;
    }

    private RawToken getRawToken(int position) {
        if (position >= mTokens.size()) return new RawToken("EOF", 0, 0);
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
        if (getNextToken() != null) revertTokenPosition();
        return result;
    }

    private Declaration parseDeclaration() {
        /* Declaration starts with `var` */
        String token = getNextToken();
        if (token == null || !token.equals("var")) {
            revertTokenPosition();
            return null;
        }

        /* Then — variable definitions */
        Declaration declaration = new Declaration(parseVariableDefinition());
        while (getNextToken().equals(",")) {
            declaration.addVariableDefinition(parseVariableDefinition());
        }
        revertTokenPosition();
        return declaration;
    }

    private Assignment parseAssignment() {
        Reference ref = parseReference();
        if (ref == null) {
            revertTokenPosition();
            return null;
        }

        /* Check for next token */
        if (!getNextToken().equals(":=")) return null;

        Expression expr = parseExpression();

        return new Assignment(ref, expr);
    }

    private If parseIf() {
        String token = getNextToken();
        /* Conditional starts with `if` */
        if (token == null || !token.equals("if")) {
            revertTokenPosition();
            return null;
        }
        Expression condition = parseExpression();

        /* Check for `then` */
        token = getNextToken();
        if (token == null || !token.equals("then")) return null;

        Body body = parseBody();

        /* Check for `else` */
        token = getNextToken();
        Body elseBody = null;
        if (token.equals("else")) {
            elseBody = parseBody();
        } else revertTokenPosition();

        /* Check for end */
        token = getNextToken();
        if (token == null || !token.equals("end")) return null;

        return new If(condition, body, elseBody);
    }

    private Loop parseLoop() {
        String token = getNextToken();
        if (token == null) return null;
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
                revertTokenPosition();
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
        revertTokenPosition();
        return null;
    }

    private Return parseReturn() {
        /* Return : return [ Expression ] */
        RawToken token = getNextRawToken();
        if(token == null || !token.val.equals("return")) {
            revertTokenPosition();
            return null;
        }
        Expression expression = parseExpression();
        if (expression == null) {
            return null;
        } else {
            return new Return(expression);
        }
    }

    private Print parsePrint() {
        /* Print : print Expression { ',' Expression } */
        RawToken token = getNextRawToken();
        if(token == null || !token.val.equals("print")) {
            revertTokenPosition();
            return null;
        }
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
        if (token.type == RawToken.TokenType.IDENTIFIER) {
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
        RawToken token = getNextRawToken();
        if (token == null || token.type != RawToken.TokenType.IDENTIFIER) return null;
        Reference result = new Reference(new Identifier(token.val));
        Tail tail;
        while((tail = parseTail()) != null) {
            result.addTail(tail);
        }
        return result;
    }

    private Tail parseTail() {
        RawToken token = getNextRawToken();
        if (token == null) return null;
        if (token.val.equals("")) {
            token = getNextRawToken();
            if(token == null) return null;
            if(token.type == RawToken.TokenType.IDENTIFIER) {
                return new NamedElementTail(new Identifier(token.val));
            }
            if(token.type == RawToken.TokenType.LITERAL) {
                revertTokenPosition();
                Literal index = parsePrimitiveLiteral();
                if (index instanceof IntegerLiteral) {
                    return new UnnamedElementTail(new IntegerLiteral(Integer.valueOf(token.val)));
                } else {
                    return null;
                }

            }
            return null;
        }
        if (token.val.equals("[")) {
            Expression expression = parseExpression();
            if (expression == null) return null;
            token = getNextRawToken();
            if (!token.val.equals("]")) return null;
            return new ArrayElementTail(expression);
        }
        if (token.val.equals("(")) {
            Expression expression = parseExpression();
            FunctionCallTail result = new FunctionCallTail(expression);
            token = getNextRawToken();
            while (token.val.equals(",")) {
                expression = parseExpression();
                if (expression == null) return null;
                result.addArgument(expression);
            }
            if (!token.val.equals(")")) return null;
            return result;
        }
        revertTokenPosition();
        return null;
    }

    private Expression parseExpression() {
        Relation relation = parseRelation();
        if(relation == null) {
            return null;
        } else {
            ExpressionComplex result = new ExpressionComplex(relation);
            String operator = getNextToken();
            if (operator == null) return null;
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
            if (isRelationOperator(operator)) {
                Factor secondFactor = parseFactor();
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
            if (operator == null) return null;
            while (isFactorSign(operator)) {
                term = parseTerm();
                if(term == null) return null;
                switch (operator) {
                    case "+": result.addTerm(term, ArithmeticOperator.ADD); break;
                    case "-": result.addTerm(term, ArithmeticOperator.SUB); break;
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
        if(token == null) return null;
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
            if (primary == null) return null;
            String nextToken = getNextToken();
            if (nextToken == null) return null;
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
            } else {
                revertTokenPosition();
                return primary;
            }
        }
        return null;
    }

    private Primary parsePrimary() {
        /* Primary : Reference | Literal | '(' Expression ')' */
        RawToken token = getNextRawToken();
        if (token.type == RawToken.TokenType.IDENTIFIER) {
            revertTokenPosition();
            return parseReference();
        } else if (token.type == RawToken.TokenType.LITERAL || token.val.equals("[") || token.val.equals("{")) {
            revertTokenPosition();
            return parseLiteral();
        } else if (token.type == RawToken.TokenType.KEYWORD && token.val.equals("func")) {
            return parseFunctionalLiteral();
        } else if (token.type == RawToken.TokenType.KEYWORD && token.val.equals("empty")) {
            return new EmptyLiteral();
        }
        return null;
    }

    private FunctionalLiteral parseFunctionalLiteral() {
        /* FunctionLiteral : func [ '(' IDENT { ',' IDENT } ')' ] FunBody */
        RawToken token = getNextRawToken();
        if (!token.val.equals("(")) return null;
        token = getNextRawToken();
        if (token.type != RawToken.TokenType.IDENTIFIER) return null;
        FunctionalLiteral result = new FunctionalLiteral(new Identifier(token.val));
        token = getNextRawToken();
        while (token.val.equals(",")) {
            token = getNextRawToken();
            if (token.type != RawToken.TokenType.IDENTIFIER) return null;
            result.addArgument(new Identifier(token.val));
        }
        if (!token.val.equals(")")) return null;
        token = getNextRawToken();

        // parseFunctionBody();
        /* FunBody : is Body end | => Expression */
        if (token.val.equals("is")) {
            Body body = parseBody();
            if (body == null) return null;
            result.setFunctionBody(new BodyFunctionBody(body));
            token = getNextRawToken();
            if (!token.val.equals("end")) return null;
            return result;
        } else if (token.val.equals(">=")){
            Expression expression = parseExpression();
            if (expression == null) return null;
            result.setFunctionBody(new LambdaFunction(expression));
            return result;
        } else {
            return null;
        }
    }

    private Literal parseLiteral() {
        /* Literal: INTEGER | REAL | STRING | Boolean | Tuple | Array | empty */
        RawToken token = getNextRawToken();
        if (token.val.equals("[")) {
            return parseArrayLiteral();
        } else if (token.val.equals("{")) {
            return parseTupleLiteral();
        } else if (token.type == RawToken.TokenType.LITERAL){
            revertTokenPosition();
            return parsePrimitiveLiteral();
        } else {
            return null;
        }
    }

    private TupleLiteral parseTupleLiteral() {
        /* Tuple : '{' TupleElement { ',' TupleElement } '}' */
        TupleElement tupleElement = parseTupleElement();
        if(tupleElement == null) {
            return null;
        }
        TupleLiteral result = new TupleLiteral(tupleElement);
        RawToken token = getNextRawToken();
        while(token.val.equals(",")) {
            tupleElement = parseTupleElement();
            if(tupleElement == null) {
                return null;
            }
            result.addElement(tupleElement);
            token = getNextRawToken();
        }
        if(!token.val.equals("}")) {
            return null;
        }
        return result;
    }

    private TupleElement parseTupleElement() {
        /* TupleElement : [ Identifier ‘:=’ ] Expression */
        RawToken token = getNextRawToken();
        if(token.type == RawToken.TokenType.IDENTIFIER) {
            Identifier identifier = new Identifier(token.val);
            token = getNextRawToken();
            if (token.val.equals(":=")) {
                Expression expression = parseExpression();
                if (expression == null) {
                    return null;
                }
                return new TupleElement(identifier, expression);
            } else {
                revertTokenPosition();
            }
        }
        revertTokenPosition();
        Expression expression = parseExpression();
        if (expression == null) {
            return null;
        }
        return new TupleElement(expression);
    }

    private ArrayLiteral parseArrayLiteral() {
        /* ArrayLiteral : '[' [ Expression { ',' Expression } ] ']' */
        RawToken token = getNextRawToken();
        if (token == null) return null;
        ArrayLiteral result = new ArrayLiteral();
        if (token.val.equals("]")) {
            return result;
        }
        revertTokenPosition();
        do {
            Expression expression = parseExpression();
            if(expression == null) {
                return null;
            }
            result.addExpression(expression);
            token = getNextRawToken();
        } while (token.val.equals(","));
        if (!token.val.equals("]")) {
            return null;
        }
        return result;
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

    // This method supposes that Lexical Analyzer gives
    // only correct literals correspondent to D language specification
    private Literal parsePrimitiveLiteral() {
        String value = getNextToken();
        if (value.startsWith("\"") || value.startsWith("'")) {
            return new StringLiteral(value);
        }
        if (value.contains(".")) {
            return new RealLiteral(Double.valueOf(value));
        }
        if (value.equals("true") || value.equals("false")) {
            return new BooleanLiteral(Boolean.valueOf(value));
        }
        return new IntegerLiteral(Integer.valueOf(value));
    }

}