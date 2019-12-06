package syntaxer.entities;

public class SignedPrimary extends Unary {
    private Primary primary;
    private UnarySign sign;

    SignedPrimary(Primary primary) {
        this.primary = primary;
        this.sign = UnarySign.NONE;
    }

    public SignedPrimary(Primary primary, UnarySign sign) {
        this.primary = primary;
        this.sign = sign;
    }

    public void setPrimary(Primary primary) {
        this.primary = primary;
    }

    public void setSign(UnarySign sign) {
        this.sign = sign;
    }

    public Primary getPrimary() {
        return primary;
    }

    public UnarySign getSign() {
        return sign;
    }

    @Override
    public String toString() {
        return "UNSIGNED PRIMARY(" + sign.toString() + primary.toString() + ")";
    }
}
