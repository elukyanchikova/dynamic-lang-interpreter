package entities;

public class IntegerLiteral extends Literal {
    Integer value;

    IntegerLiteral(Integer value) {
        this.value = value;
    }

    IntegerLiteral(int value) {
        this.value = value;
    }
}
