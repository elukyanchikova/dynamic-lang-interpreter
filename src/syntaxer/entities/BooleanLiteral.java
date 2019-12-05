package syntaxer.entities;

public class BooleanLiteral extends Literal {
    private Boolean value;

    public BooleanLiteral(Boolean value) {
        this.value = value;
    }

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
