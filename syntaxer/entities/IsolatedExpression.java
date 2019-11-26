package entities;

public class IsolatedExpression extends Unary {
    Expression expression;

    public IsolatedExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
}
