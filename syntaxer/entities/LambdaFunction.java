package entities;

public class LambdaFunction extends FunctionBody {
    Expression expression;

    public LambdaFunction(Expression expression) {
        this.expression = expression;
    }
}
