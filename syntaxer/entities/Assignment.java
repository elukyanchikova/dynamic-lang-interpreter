package entities;

public class Assignment extends Statement {
    private Reference reference;
    private Expression expression;

    public Assignment(Reference reference, Expression expression) {
        this.reference = reference;
        this.expression = expression;
    }

    /**
     * @return the expression
     */
    public Expression getExpression() {
        return expression;
    }

    /**
     * @return the reference
     */
    public Reference getReference() {
        return reference;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }
}
