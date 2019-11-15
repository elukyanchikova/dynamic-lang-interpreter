package entities;

public class VariableDefinition extends Statement {
    Identifier identifier;
    Expression expression;

    VariableDefinition(Identifier identifier) {
        this.identifier = identifier;
        this.expression = new Expression(new Relation(new Factor(new Term(new EmptyLiteral()))));
    }

    VariableDefinition(Identifier identifier, Expression expression) {
        this.identifier = identifier;
        this.expression = expression;
    }
}
