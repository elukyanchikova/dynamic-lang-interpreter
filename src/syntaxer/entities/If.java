package syntaxer.entities;

public class If extends Statement {
    private Expression condition;
    private Body thenBody;
    private Body elseBody;

    public If(Expression condition, Body thenBody, Body elseBody) {
        this.condition = condition;
        this.thenBody = thenBody;
        this.elseBody = elseBody;
    }

    /**
     * @return the condition
     */
    public Expression getCondition() {
        return condition;
    }

    /**
     * @return the thenBody
     */
    public Body getThenBody() {
        return thenBody;
    }

    /**
     * @return the elseBody
     */
    public Body getElseBody() {
        return elseBody;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        String res = "if (COND: " + condition.toString() + ") then\n" + thenBody.toString() + "else\n";
        if (elseBody != null) res = res + elseBody.toString();
        return res;
    }
}
