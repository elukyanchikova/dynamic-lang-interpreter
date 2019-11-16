package entities;

public class LambdaFunction extends FunctionBody {
    Expression expression;

    LambdaFunction(Expression expression) {
        this.expression = expression;
    }
}
