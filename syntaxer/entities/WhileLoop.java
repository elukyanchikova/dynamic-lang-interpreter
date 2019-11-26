package entities;

public class WhileLoop extends Loop {
    private Expression mCondition;
    private Body mBody;

    public WhileLoop(Expression condition, Body body) {
        this.mCondition = condition;
        this.mBody = body;
    }

    /**
     * @return the body
     */
    public Body getBody() {
        return mBody;
    }

    /**
     * @return the condition
     */
    public Expression getCondition() {
        return mCondition;
    }
}
