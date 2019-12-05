package syntaxer.entities;

public class TupleElement {
    private Identifier identifier;
    private Expression expression;

    public TupleElement(Expression expression) {
        this.expression = expression;
        identifier = null;
    }

    public TupleElement(Identifier identifier, Expression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }

    public Expression getExpression() {
        return expression;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }
}
