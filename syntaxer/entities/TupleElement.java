package entities;

public class TupleElement {
    Identifier identifier;
    Expression expression;

    TupleElement(Expression expression) {
        this.expression = expression;
        identifier = null;
    }
}
