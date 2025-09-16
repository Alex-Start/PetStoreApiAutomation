package verify;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AssertErrorsContainer {
    private final List<AssertErrorContainer> containerList = new ArrayList<>();

    public AssertErrorsContainer() {

    }

    public AssertErrorsContainer add(AssertErrorContainer assertErrorContainer) {
        containerList.add(assertErrorContainer);
        return this;
    }

    public boolean isPassed() {
        return containerList.stream().allMatch(AssertErrorContainer::isPassed);
    }

    public String getErrorMessage() {
        return containerList.stream().map(AssertErrorContainer::getErrorMessage).collect(Collectors.joining(", "));
    }
}
