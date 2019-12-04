package syntaxer.entities;

public class ArrayElementTail extends Tail {
    private Expression expression;

    public ArrayElementTail(Expression expression) {
        this.expression = expression;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }
}
