package verify;

/**
 * Verify as String
 */
public class StringVerifier implements IVerifier {
    private final String str;

    public static StringVerifier asString(String string) {
        return new StringVerifier(string);
    }

    private StringVerifier(String string) {
        this.str = string;
    }

    public boolean verify(Object obj) {
        String value = (String) obj;
        if (str == null && value == null) {
            return true;
        }
        if (str == null || value == null) {
            return false;
        }
        return str.equals(value);
    }

    public Object getValue() {
        return str;
    }
}
