package entities;

public class If {
    Expression condition;
    Body thenBody;
    Body elseBody;

    If(Expression condition, Body thenBody, Body elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    If(Expression condition, Body thenBody) {
        this.condition = condition;
        this.thenBody = thenBody;
    }
}
