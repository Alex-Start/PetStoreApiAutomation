package verify;

/**
 * Verify as number
 */
public class NumberVerifier implements IVerifier {
    private final Number number;

    public static NumberVerifier asNumber(Number number) {
        return new NumberVerifier(number);
    }

    private NumberVerifier(Number number) {
        this.number = number;
    }

    public boolean verify(Object value) {
        Number number = (Number)value;
        if (this.number == null && number == null) {
            return true;
        }
        if (this.number == null || number == null) {
            return false;
        }
        return this.number.equals(number);
    }

    public Number getValue() {
        return number;
    }
}
