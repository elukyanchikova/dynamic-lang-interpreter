package syntaxer.entities;

public class UnnamedElementTail extends Tail {
    private IntegerLiteral integer;

    public UnnamedElementTail(IntegerLiteral integer) {
        this.integer = integer;
    }

    public IntegerLiteral getInteger() {
        return integer;
    }
}
