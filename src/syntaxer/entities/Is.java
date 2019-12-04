package syntaxer.entities;

public class Is extends Unary {
    private Expression primary;
    private TypeIndicator typeIndicator;

    public Is(Expression primary, TypeIndicator typeIndicator) {
        this.primary = primary;
        this.typeIndicator = typeIndicator;
    }

    public void setPrimary(Expression primary) {
        this.primary = primary;
    }

    public Expression getPrimary() {
        return primary;
    }
}
