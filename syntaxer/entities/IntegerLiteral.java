package entities;

public class IntegerLiteral extends Literal {
    Integer value;

    public IntegerLiteral(Integer value) {
        this.value = value;
    }

    IntegerLiteral(int value) {
        this.value = value;
    }
}
