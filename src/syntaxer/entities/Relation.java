package syntaxer.entities;

public class Relation {
    Factor firstFactor;
    RelationOperator operator;
    Factor secondFactor;

    public Relation(Factor firstFactor, RelationOperator operator, Factor secondFactor) {
        this.firstFactor = firstFactor;
        this.operator = operator;
        this.secondFactor = secondFactor;
    }

    public Relation(Factor firstFactor) {
        this.firstFactor = firstFactor;
        operator = null;
        secondFactor = null;
    }
}
