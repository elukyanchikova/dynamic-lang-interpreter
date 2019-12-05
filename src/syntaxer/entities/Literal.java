package syntaxer.entities;

public class Literal extends Primary {

    public static enum LiteralType {
        STRING,
        INTEGER,
        REAL,
        BOOLEAN
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
