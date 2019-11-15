package entities;

public class Assignment extends Statement {
    Reference reference;
    Expression expression;

    Assignment(Reference reference, Expression expression) {
        this.reference = reference;
        this.expression = expression;
    }
}
