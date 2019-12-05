package syntaxer.entities;

public class Literal extends Primary implements Cloneable {

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

    @Override
    public Literal clone() throws CloneNotSupportedException {
        return (Literal) super.clone();
    }
}
