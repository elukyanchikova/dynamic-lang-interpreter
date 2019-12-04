package syntaxer.entities;

public class IsolatedExpression extends Unary {
    private Expression expression;

    public IsolatedExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }
}
