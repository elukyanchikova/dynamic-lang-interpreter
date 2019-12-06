package syntaxer.entities;

public class Identifier extends Primary {
    private String value;

    public Identifier(String value) {
        this.value = value;
    }

    public String getName() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
