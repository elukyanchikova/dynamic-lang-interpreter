package syntaxer.entities;

public class NamedElementTail extends Tail {
    private Identifier identifier;

    public NamedElementTail(Identifier identifier) {
        this.identifier = identifier;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}
