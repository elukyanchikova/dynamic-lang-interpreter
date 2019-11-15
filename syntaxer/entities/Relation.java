package entities;

public class Relation {
    Factor firstFactor;
    RelationOperator operator;
    Factor secondFactor;

    Relation(Factor firstFactor, RelationOperator operator, Factor secondFactor) {
        this.firstFactor = firstFactor;
        this.operator = operator;
        this.secondFactor = secondFactor;
    }

    Relation(Factor firstFactor) {
        this.firstFactor = firstFactor;
        operator = null;
        secondFactor = null;
    }
}
