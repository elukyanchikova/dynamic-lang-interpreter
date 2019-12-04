package entities;

public class Range {
    private Expression start;
    private Expression end;

    public Range(Expression start, Expression end) {
        this.start = start;
        this.end = end;
    }

    public Expression getStart() {
        return start;
    }

    public Expression getEnd() {
        return end;
    }

    public void setStart(Expression start) {
        this.start = start;
    }

    public void setEnd(Expression end) {
        this.end = end;
    }
}
