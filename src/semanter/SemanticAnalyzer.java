package semanter;

import syntaxer.entities.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer {

    private Program ast;

    public SemanticAnalyzer(Program ast) throws IOException {
        this.ast = ast;
        List<Statement> statements = ast.getStatements();
        simplify(statements);
    }

    public Program getAst() {
        return ast;
    }

    public void simplify(List<Statement> statements) {
        for(Statement s: statements) {
            if(s instanceof Declaration) {
                List<VariableDefinition> varDefs = ((Declaration) s).getVariableDefinitions();
                for(VariableDefinition vd: varDefs) {
                    Expression expression = vd.getExpression();
                    expression = simplifyExpression(expression);
                    vd.setExpression(expression);
                }
            } else if (s instanceof Assignment) {
                ((Assignment) s).setExpression(((Assignment) s).getExpression());
            } else if (s instanceof If) {
                ((If) s).setCondition(simplifyExpression(((If) s).getCondition()));
                Body body = ((If) s).getThenBody();
                if(body != null) {
                    List<Statement> bodyStatements = body.getStatements();
                    simplify(bodyStatements);
                }
                body = ((If) s).getElseBody();
                if(body != null) {
                    List<Statement> bodyStatements = body.getStatements();
                    simplify(bodyStatements);
                }
            } else if (s instanceof ForLoop) {
                Range range = ((ForLoop) s).getRange();
                range.setStart(simplifyExpression(range.getStart()));
                range.setEnd(simplifyExpression(range.getEnd()));
                simplify(((ForLoop) s).getBody().getStatements());
            } else if (s instanceof WhileLoop) {
                ((WhileLoop) s).setCondition(((WhileLoop) s).getCondition());
                simplify(((WhileLoop) s).getBody().getStatements());
            } else if (s instanceof Print) {
                List<Expression> newExpression = new ArrayList<>(((Print) s).getExpressions().size());
                for(Expression expression: ((Print) s).getExpressions()) {
                    newExpression.add(simplifyExpression(expression));
                }
                ((Print) s).setExpressions(newExpression);
            } else if (s instanceof Return) {
                ((Return) s).setExpression(((Return) s).getExpression());
            }

        }
    }

    /**
     * Simplifies complex structure of language expressions converting it to more simple structures
     *
     * For example:
     *
     * Expression -> Relation -> Factor -> Term -> Unary -> Primary -> Literal -> IntegerLiteral(5)
     * to:
     * Integer(5)
     *
     * @param expression - complex expression which is going to be simplified
     * @return - the most possible simplified form of expression
     */
    private Expression simplifyExpression(Expression expression) {
        if(expression instanceof ExpressionComplex) {
            List<Expression> relations = ((ExpressionComplex) expression).getRelationList();
            if(relations.size() > 1) {
                for(int i = 0; i < relations.size(); i++) {
                    relations.set(i, simplifyRelation(relations.get(i)));
                }
            } else {
                return simplifyRelation(relations.get(0));
            }
        }
        return expression;
    }

    private Expression simplifyRelation(Expression relation) {
        if (relation instanceof Relation) {
            if (((Relation) relation).getSecondFactor() == null) {
                return simplifyFactor(((Relation) relation).getFirstFactor());
            } else {
                ((Relation) relation).setFirstFactor(simplifyFactor(((Relation) relation).getFirstFactor()));
                ((Relation) relation).setSecondFactor(simplifyFactor(((Relation) relation).getSecondFactor()));
                return relation;
            }
        } else {
            System.out.println("Not Relation class were received by simplifyRelation method");
            return null;
        }
    }

    private Expression simplifyFactor(Expression factor) {
        if (factor instanceof Factor) {
            List<Expression> terms = ((Factor) factor).getTerms();
            if (terms.size() > 1) {
                for(int i = 0; i < terms.size(); i++) {
                    terms.set(i, simplifyTerm(terms.get(i)));
                }
            } else {
                return simplifyTerm(terms.get(0));
            }
            return factor;
        } else {
            return null;
        }
    }

    private Expression simplifyTerm(Expression term) {
        if (term instanceof Term) {
            List<Expression> unaryList = ((Term) term).getUnaryList();
            if (unaryList.size() > 1) {
                for(int i = 0; i < unaryList.size(); i++) {
                    unaryList.set(i, simplifyUnary(unaryList.get(i)));
                }
            } else {
                return simplifyUnary(unaryList.get(0));
            }
            return term;
        } else {
            return null;
        }
    }

    private Expression simplifyUnary(Expression unary) {
        if (unary instanceof Unary) {
            if (unary instanceof IsolatedExpression) {
                ((IsolatedExpression) unary).setExpression(((IsolatedExpression) unary).getExpression());
                return unary;
            } else if (unary instanceof Is) {
                ((Is) unary).setPrimary(simplifyPrimary(((Is) unary).getPrimary()));
                return unary;
            } else if (unary instanceof Primary) {
                return simplifyPrimary(unary);
            } else if (unary instanceof SignedPrimary) {
                ((SignedPrimary) unary).setPrimary(((SignedPrimary) unary).getPrimary());
                return unary;
            }
            return unary;
        } else {
            return null;
        }
    }

    private Expression simplifyPrimary(Expression primary) {
        if (primary instanceof Primary) {
            if (primary instanceof FunctionalLiteral) {
                FunctionBody body = ((FunctionalLiteral) primary).getFunctionBody();
                if (body instanceof LambdaFunction) {
                    ((LambdaFunction) body).setExpression(simplifyExpression(((LambdaFunction) body).getExpression()));
                } else if (body instanceof BodyFunctionBody) {
                    simplify(((BodyFunctionBody) body).getBody().getStatements());
                }
                return primary;
            } else if (primary instanceof Reference) {
                if (((Reference) primary).getTailList().isEmpty()) {
                    return ((Reference) primary).getIdentifier();
                } else {
                    List<Tail> tails = ((Reference) primary).getTailList();
                    for(int i = 0; i < tails.size(); i++) {
                        tails.set(i, simplifyTail(tails.get(i)));
                    }
                    return primary;
                }
            } else if (primary instanceof Literal) {
                return simplifyLiteral(primary);
            } else {
                return primary;
            }
        } else {
            return null;
        }
    }

    private Tail simplifyTail(Tail tail) {
        if (tail instanceof ArrayElementTail) {
            ((ArrayElementTail) tail).setExpression(simplifyExpression(((ArrayElementTail) tail).getExpression()));
        } else if (tail instanceof FunctionCallTail) {
            List<Expression> arguments = ((FunctionCallTail) tail).getArguments();
            for (int i = 0; i < arguments.size(); i++) {
                arguments.set(i, simplifyExpression(arguments.get(i)));
            }
        }
        return tail;
    }

    private Expression simplifyLiteral(Expression literal) {
        if (literal instanceof Literal) {
            return literal;
        } else {
            return null;
        }
    }
}
