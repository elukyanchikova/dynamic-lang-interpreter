package syntaxer.entities;

public class Return extends Statement {
    private Expression expression;

    public Return(Expression expression) {
        this.expression = expression;
    }

    /**
     * @return the expression
     */
    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }
}
