package syntaxer.entities;

public class VariableDefinition extends Statement {
    private Identifier mIdentifier;
    private Expression mExpression;

    public VariableDefinition(Identifier identifier) {
        this.mIdentifier = identifier;
        this.mExpression = new ExpressionComplex(new Relation(new Factor(new Term(new EmptyLiteral()))));
    }

    VariableDefinition(Identifier identifier, Expression expression) {
        this.mIdentifier = identifier;
        this.mExpression = expression;
    }

    /**
     * @return the expression
     */
    public Expression getExpression() {
        return mExpression;
    }

    /**
     * @param expression the expression to set
     */
    public void setExpression(Expression expression) {
        this.mExpression = expression;
    }

    /**
     * @return the identifier
     */
    public Identifier getIdentifier() {
        return mIdentifier;
    }
}
