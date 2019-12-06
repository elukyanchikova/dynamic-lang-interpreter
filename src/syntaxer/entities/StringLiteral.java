package syntaxer.entities;

public class StringLiteral extends Literal {
    private String value;

    public StringLiteral(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return '\'' + value + '\'';
    }
}
