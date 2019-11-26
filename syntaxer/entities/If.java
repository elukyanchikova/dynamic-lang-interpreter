package entities;

public class If extends Statement {
    private Expression mCondition;
    private Body mThenBody;
    private Body mElseBody;

    public If(Expression condition, Body thenBody, Body elseBody) {
        this.mCondition = condition;
        this.mThenBody = thenBody;
        this.mElseBody = elseBody;
    }

    /**
     * @return the condition
     */
    public Expression getCondition() {
        return mCondition;
    }

    /**
     * @return the thenBody
     */
    public Body getThenBody() {
        return mThenBody;
    }

    /**
     * @return the elseBody
     */
    public Body getElseBody() {
        return mElseBody;
    }
}
