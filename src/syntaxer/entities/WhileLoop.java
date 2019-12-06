package syntaxer.entities;

public class WhileLoop extends Loop {
    private Expression condition;
    private Body body;

    public WhileLoop(Expression condition, Body body) {
        this.condition = condition;
        this.body = body;
    }

    /**
     * @return the body
     */
    public Body getBody() {
        return body;
    }

    /**
     * @return the condition
     */
    public Expression getCondition() {
        return condition;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Statement s: body.getStatements()) {
            sb.append(s.toString());
        }
        return sb.toString();
    }
}
