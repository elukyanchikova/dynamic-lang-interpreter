package entities;

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
}
