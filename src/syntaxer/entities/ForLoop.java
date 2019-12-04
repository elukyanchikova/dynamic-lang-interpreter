package syntaxer.entities;

public class ForLoop extends Loop {
    private Identifier mCounter;
    private Range mRange;
    private Body mBody;

    public ForLoop(Body body, Range range, Identifier counter) {
        this.mCounter = counter;
        this.mRange = range;
        this.mBody = body;
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
        return mCounter;
    }

    /**
     * @return the range
     */
    public Range getRange() {
        return mRange;
    }

    /**
     * @return the body
     */
    public Body getBody() {
        return mBody;
    }
}
