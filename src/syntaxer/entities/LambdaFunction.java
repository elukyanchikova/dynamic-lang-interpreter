package syntaxer.entities;

public class LambdaFunction extends FunctionBody {
    private Expression expression;

    public LambdaFunction(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }
}
