package syntaxer.entities;

public class ForLoop extends Loop {
    private Identifier counter;
    private Range range;
    private Body body;

    public ForLoop(Body body, Range range, Identifier counter) {
        this.counter = counter;
        this.range = range;
        this.body = body;
    }

    public ForLoop(Body body, Range range) {
        this(body, range, null);
    }

    public ForLoop(Body body) {
        this(body, null, null);
    }

    /**
     * @return the counter
     */
    public Identifier getCounter() {
        return counter;
    }

    /**
     * @return the range
     */
    public Range getRange() {
        return range;
    }

    /**
     * @return the body
     */
    public Body getBody() {
        return body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("for ");
        if (counter != null)
            sb.append("(COUNTER: ").append(counter.getName()).append(") ");
        if (range != null) {
            sb.append(range.toString());
        }
        sb.append("\n");
        for(Statement s: body.getStatements()) {
            sb.append(s.toString());
        }
        return sb.toString();
    }
}
