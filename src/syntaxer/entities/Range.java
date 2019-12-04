package syntaxer.entities;

public class Range {
    Expression start;
    Expression end;

    public Range(Expression start, Expression end) {
        this.start = start;
        this.end = end;
    }
}
