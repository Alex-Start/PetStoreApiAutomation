package verify;

import io.restassured.response.Response;

/**
 * Collect all objects to manage it in tests - mediator
 */
public class AssertErrorContainer {

    private boolean isPassed = false;
    private String errorMessage;
    private int actualResultInt;
    private final Response response;

    public AssertErrorContainer(Response response) {
        this.response = response;
    }

    public AssertErrorContainer passed() {
        isPassed = true;
        return this;
    }

    public AssertErrorContainer failed(String message) {
        errorMessage = message;
        isPassed = false;
        return this;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public AssertErrorContainer withActualResultInt(int actualResult) {
        this.actualResultInt = actualResult;
        return this;
    }

    public int getActualResultInt() {
        return actualResultInt;
    }

    public Response getResponse() {
        return response;
    }

}
