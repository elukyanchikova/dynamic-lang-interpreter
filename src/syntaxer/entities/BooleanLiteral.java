package syntaxer.entities;

public class BooleanLiteral extends Literal {
    Boolean value;

    public BooleanLiteral(Boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
