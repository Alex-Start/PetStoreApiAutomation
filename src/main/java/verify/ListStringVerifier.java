package verify;

import java.util.List;

/**
 * Verify as List<String></String>
 */
public class ListStringVerifier implements IVerifier {
    private final List<String> stringList;

    public static ListStringVerifier asListString(List<String> list) {
        return new ListStringVerifier(list);
    }

    private ListStringVerifier(List<String> list) {
        this.stringList = list;
    }

    public boolean verify(Object obj) {
        List<String> value = (List<String>) obj;
        if (stringList == null && value == null) {
            return true;
        }
        if (stringList == null || value == null) {
            return false;
        }
        return stringList.equals(value);
    }

    public List<String> getValue() {
        return stringList;
    }
}
