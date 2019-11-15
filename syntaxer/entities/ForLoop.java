package entities;

public class ForLoop extends Loop {
    Identifier counter;
    Range range;
    Body body;

    ForLoop(Identifier counter, Range range, Body body) {
        this.counter = counter;
        this.range = range;
        this.body = body;
    }
}
