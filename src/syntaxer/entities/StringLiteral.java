package syntaxer.entities;

public class StringLiteral extends Literal {
    String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
