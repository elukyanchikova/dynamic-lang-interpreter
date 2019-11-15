package entities;

public class Range {
    Expression start;
    Expression end;

    Range(Expression start, Expression end) {
        this.start = start;
        this.end = end;
    }
}
