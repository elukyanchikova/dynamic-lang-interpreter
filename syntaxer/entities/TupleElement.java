package entities;

public class TupleElement {
    Identifier identifier;
    Expression expression;

    public TupleElement(Expression expression) {
        this.expression = expression;
        identifier = null;
    }

    public TupleElement(Identifier identifier, Expression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }
}
