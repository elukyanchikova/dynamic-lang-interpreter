package entities;

public class RealLiteral extends Literal {
    Double value;

    RealLiteral(Double value) {
        this.value = value;
    }

    RealLiteral(double value) {
        this.value = value;
    }
}
