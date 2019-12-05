package syntaxer.entities;

public class RealLiteral extends Literal {
    Double value;

    public RealLiteral(Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
