package entities;

public class Is extends Unary {
    Primary primary;
    TypeIndicator typeIndicator;

    public Is(Primary primary, TypeIndicator typeIndicator) {
        this.primary = primary;
        this.typeIndicator = typeIndicator;
    }
}
