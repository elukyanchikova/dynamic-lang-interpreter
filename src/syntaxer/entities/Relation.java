package syntaxer.entities;

public class Relation extends Expression {
    private Expression firstFactor;
    private RelationOperator operator;
    private Expression secondFactor;

    public Relation(Expression firstFactor, RelationOperator operator, Expression secondFactor) {
        this.firstFactor = firstFactor;
        this.operator = operator;
        this.secondFactor = secondFactor;
    }

    public Relation(Expression firstFactor) {
        this.firstFactor = firstFactor;
        operator = null;
        secondFactor = null;
    }

    public Expression getFirstFactor() {
        return firstFactor;
    }

    public Expression getSecondFactor() {
        return secondFactor;
    }

    public void setFirstFactor(Expression firstFactor) {
        this.firstFactor = firstFactor;
    }

    public void setSecondFactor(Expression secondFactor) {
        this.secondFactor = secondFactor;
    }

    public RelationOperator getOperator() {
        return operator;
    }
}
