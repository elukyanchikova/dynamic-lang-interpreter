package syntaxer.entities;

public class IntegerLiteral extends Literal {
    private Integer value;

    public IntegerLiteral(Integer value) {
        this.value = value;
    }

    IntegerLiteral(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
