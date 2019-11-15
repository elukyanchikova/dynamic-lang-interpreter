package entities;

public class WhileLoop extends Loop {
    Expression condition;
    Body body;

    WhileLoop(Expression condition, Body body) {
        this.condition = condition;
        this.body = body;
    }
}
