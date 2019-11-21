package entities;

public class SignedPrimary extends Unary {
    Primary primary;
    UnarySign sign;

    SignedPrimary(Primary primary) {
        this.primary = primary;
        this.sign = UnarySign.NONE;
    }

    public SignedPrimary(Primary primary, UnarySign sign) {
        this.primary = primary;
        this.sign = sign;
    }
}
