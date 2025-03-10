package verify;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Verify as regexp
 */
public class RegExpVerifier implements IVerifier {
    private final String regexp;
    private int caseSensitive = Pattern.CASE_INSENSITIVE;

    public static RegExpVerifier asRegexp(String pattern) {
        return new RegExpVerifier(pattern);
    }

    private RegExpVerifier(String pattern) {
        regexp = pattern;
    }

    public RegExpVerifier(String pattern, int caseSensitive) {
        regexp = pattern;
        this.caseSensitive = caseSensitive;
    }

    public boolean verify(Object value) {
        Pattern pattern = Pattern.compile(regexp, caseSensitive);
        Matcher matcher = pattern.matcher((String)value);
        return matcher.find();
    }

    public String getValue() {
        return regexp;
    }
}
