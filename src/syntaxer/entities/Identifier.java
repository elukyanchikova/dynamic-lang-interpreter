package syntaxer.entities;

public class Identifier {
    private String value;

    public Identifier(String value) {
        this.value = value;
    }

    public String getName() {
        return value;
    }
}
