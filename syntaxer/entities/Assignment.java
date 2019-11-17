package entities;

public class Assignment extends Statement {
    private Reference mReference;
    private Expression mExpression;

    public Assignment(Reference reference, Expression expression) {
        this.mReference = reference;
        this.mExpression = expression;
    }

    /**
     * @return the expression
     */
    public Expression getExpression() {
        return mExpression;
    }

    /**
     * @return the reference
     */
    public Reference getReference() {
        return mReference;
    }
}
