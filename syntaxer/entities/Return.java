package entities;

public class Return extends Statement {
    private Expression mExpression;

    public Return(Expression expression) {
        this.expression = expression;
    }

    /**
     * @return the expression
     */
    public Expression getExpression() {
        return mExpression;
    }
}
