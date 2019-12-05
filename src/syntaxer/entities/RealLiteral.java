package syntaxer.entities;

public class RealLiteral extends Literal {
    Double value;

    public RealLiteral(Double value) {
        this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
}
