package verify;

import api.ResponseHelper;
import io.restassured.response.Response;
import model.*;
import model.Order;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.*;
import utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Verify Response object with IModel
 */
public class VerifierResponse {
    private static final Logger logger = LogManager.getLogger(VerifierResponse.class);

    private static final int TIMEOUT_MILLISEC_WAIT_RESPONSE = 3000;
    // WA: try to call and check repeatedly to avoid below-mentioned issue:
    //2025-03-07 06:08:41 [main] INFO  verify.PetVerifier - Actual Result: {code=1, type=error, message=Pet not found}
    //java.lang.AssertionError: FAILED: Status Code = 200 for Pet response
    //Expected :200
    //Actual   :404
    public static AssertErrorsContainer verify(IModel expected, Callable<Response> actualResponse) {

        AtomicReference<Response> response = new AtomicReference<>();
        Callable<Boolean> func = ()->{
            try {
                response.set(actualResponse.call());
                verify(expected, response.get());
            } catch (AssertionError e) {
                return false;
            }
            return true;
        };
        // Disable logging for loop execution.
        // Set the root logger level to WARN (ignores INFO and DEBUG)
        Configurator.setRootLevel(Level.WARN);
        Util.waitUntilFuncTrue(func, TIMEOUT_MILLISEC_WAIT_RESPONSE);
        Configurator.setRootLevel(Level.INFO);

        // call once to log action - there is only get action
        try {
            response.set(actualResponse.call());
        } catch (Exception e) {
            //
        }
        return verify(expected, response.get());
    }

    public static AssertErrorsContainer verify(IModel expected, Response actualResponse) {
        if (expected instanceof Pet) {
            return verifyPet((Pet) expected, actualResponse);
        } else if (expected instanceof User) {
            return verifyUser((User) expected, actualResponse);
        } else if (expected instanceof CommonResponse) {
            return verifyCommonResponse((CommonResponse) expected, actualResponse);
        } else if (expected instanceof CommonResponseComparator) {
            return verifyCommonResponse((CommonResponseComparator) expected, actualResponse);}
        else if (expected instanceof Order) {
            return verifyOrder((Order) expected, actualResponse);
        } else {
            throw new IllegalArgumentException("Unsupported model type: " + expected.getClass().getSimpleName());
        }
    }

    public static AssertErrorsContainer verifyPet(Pet expected, Response actualResponse) {
        expected.updateId(ResponseHelper.getId(actualResponse));
        logInfoMessage("=== Verify Pet: {} ===", expected);
        logInfoMessage("Actual Result: {}", ResponseHelper.getBodyJsonString(actualResponse));
        AssertErrorsContainer assertErrorsContainer = new AssertErrorsContainer().add(verifyStatusCode(actualResponse));

        List<String> errors = new ArrayList<>();

        // Extract values from API response
        //Object actualId = actualResponse.jsonPath().getInt("id");
        Object category = actualResponse.jsonPath().get("category");
        int actualCategoryId = 0;
        String actualCategoryName = null;
        if (category != null) {
            actualCategoryId = actualResponse.jsonPath().getInt("category.id");
            actualCategoryName = actualResponse.jsonPath().getString("category.name");
        }
        String actualName = actualResponse.jsonPath().getString("name");
        List<String> actualPhotoUrls = actualResponse.jsonPath().getList("photoUrls");
        List<Map<String, Object>> actualTags = actualResponse.jsonPath().getList("tags");
        String actualStatus = actualResponse.jsonPath().getString("status");

        if (expected.getCategory() != null) {
            verifyInt("Category ID", expected.getCategory().getId(), actualCategoryId, errors);
            verify("Category Name", expected.getCategory().getName(), actualCategoryName, errors);
        } else {
            verifyInt("Category ID", 0, actualCategoryId, errors);
            verify("Category Name", null, actualCategoryName, errors);
        }
        verify("Pet Name", expected.getName(), actualName, errors);
        verify("Photo URLs", expected.getPhotoUrls(), actualPhotoUrls, errors);
        if (expected.getStatus() != null) {
            verify("Pet Status", expected.getStatus().toString(), actualStatus, errors);
        } else {
            verify("Pet Status", null, actualStatus, errors);
        }


        // Verify tags
        if (expected.getTags() != null) {
            verifyInt("Tag count", expected.getTags().size(), actualTags.size(), errors);
            for (int i = 0; i < expected.getTags().size(); i++) {
                int expectedTagId = expected.getTags().get(i).getId();
                String expectedTagName = expected.getTags().get(i).getName();
                // TODO find actual by expected id to match
                if (i >= actualTags.size()) {
                    continue;
                }
                int actualTagId = (int) actualTags.get(i).get("id");
                String actualTagName = (String) actualTags.get(i).get("name");

                verifyInt("Tag ID at index " + i, expectedTagId, actualTagId, errors);
                verify("Tag Name at index " + i, expectedTagName, actualTagName, errors);
            }

        }

        // If errors exist, fail with all mismatches
        if (!errors.isEmpty()) {
            return assertErrorsContainer.add(new AssertErrorContainer(actualResponse).failed("Pet verification failed:\n" + String.join("\n", errors)));
        }
        return assertErrorsContainer.add(new AssertErrorContainer(actualResponse).passed());
    }

    public static AssertErrorsContainer verifyUser(User expected, Response actualResponse) {
        expected.updateId(ResponseHelper.getId(actualResponse));
        logInfoMessage("=== Verify User: {} ===", expected);
        logInfoMessage("Actual Result: {}", ResponseHelper.getBodyJsonString(actualResponse));
        AssertErrorsContainer assertErrorsContainer = new AssertErrorsContainer().add(verifyStatusCode(actualResponse));

        List<String> errors = new ArrayList<>();

        // Extract values from API response
        //Object actualId = actualResponse.jsonPath().get("id");
        String actualUsername = actualResponse.jsonPath().getString("username");
        String actualFirstname = actualResponse.jsonPath().getString("firstname");
        String actualLastname = actualResponse.jsonPath().getString("lastname");
        String actualEmail = actualResponse.jsonPath().getString("email");
        String actualPassword = actualResponse.jsonPath().getString("password");
        String actualPhone = actualResponse.jsonPath().getString("phone");
        int actualUserStatus = 0;
        if (actualResponse.jsonPath().get("userStatus") != null) {
            actualUserStatus = actualResponse.jsonPath().getInt("userStatus");
        }

        // Perform verifications
        //verifyNumber("User ID", expected.getId(), actualId, errors);
        verify("Username", expected.getUsername(), actualUsername, errors);
        verify("Firstname", expected.getFirstname(), actualFirstname, errors);
        verify("Lastname", expected.getLastname(), actualLastname, errors);
        verify("Email", expected.getEmail(), actualEmail, errors);
        verify("Password", expected.getPassword(), actualPassword, errors);
        verify("Phone", expected.getPhone(), actualPhone, errors);
        verifyInt("User Status", expected.getUserStatus(), actualUserStatus, errors);

        // If errors exist, fail with all mismatches
        if (!errors.isEmpty()) {
            return assertErrorsContainer.add(new AssertErrorContainer(actualResponse).failed("User verification failed:\n" + String.join("\n", errors)));
        }
        return assertErrorsContainer.add(new AssertErrorContainer(actualResponse).passed());
    }

    private static AssertErrorsContainer verifyOrder(Order expected, Response actualResponse) {
        expected.updateId(ResponseHelper.getId(actualResponse));
        logInfoMessage("=== Verify Order: {} ===", expected);
        logInfoMessage("Actual Result: {}", ResponseHelper.getBodyJsonString(actualResponse));
        AssertErrorsContainer assertErrorsContainer = new AssertErrorsContainer().add(verifyStatusCode(actualResponse));

        List<String> errors = new ArrayList<>();

        // Extract values from API response
        //Object actualId = actualResponse.jsonPath().get("id");
        Object actualPetId = actualResponse.jsonPath().get("petId");
        Object quantity = actualResponse.jsonPath().get("quantity");
        int actualQuantity = 0;
        if (quantity != null) {
            actualQuantity = actualResponse.jsonPath().getInt("quantity");
        }
        String actualShipDate = actualResponse.jsonPath().getString("shipDate");
        String actualStatus = actualResponse.jsonPath().getString("status");
        boolean actualComplete = false;
        if (actualResponse.jsonPath().get("complete") != null) {
            // probably need to verify as Object
            actualComplete = actualResponse.jsonPath().getBoolean("complete");
        }

        // Perform verifications
        //verifyNumber("Order ID", expected.getId(), actualId, errors);
        verify("petId", expected.getPetId(), actualPetId, errors);
        verifyInt("quantity", expected.getQuantity(), actualQuantity, errors);
        verify("shipDate", expected.getShipDate(), actualShipDate, errors);
        verify("status", expected.getStatus(), actualStatus, errors);
        verifyBool("complete", expected.isComplete(), actualComplete, errors);

        // If errors exist, fail with all mismatches
        if (!errors.isEmpty()) {
            return assertErrorsContainer.add(new AssertErrorContainer(actualResponse).failed("User verification failed:\n" + String.join("\n", errors)));
        }
        return assertErrorsContainer.add(new AssertErrorContainer(actualResponse).passed());
    }

    public static AssertErrorsContainer verifyCommonResponse(CommonResponse expected, Response actualResponse) {

        logInfoMessage("=== Verify Response: {} ===", expected);
        logInfoMessage("Actual Result: {}", ResponseHelper.getBodyJsonString(actualResponse));
        AssertErrorsContainer assertErrorsContainer = new AssertErrorsContainer().add(verifyStatusCode(actualResponse));

        List<String> errors = new ArrayList<>();

        // Extract values from API response
        Number code = actualResponse.jsonPath().get("code");
        String type = actualResponse.jsonPath().getString("type");
        String message = actualResponse.jsonPath().getString("message");

        // Perform verifications
        verify("Code", expected.code(), code, errors);
        verify("Type", expected.type(), type, errors);
        verify("Message", expected.message(), message, errors);

        // If errors exist, fail with all mismatches
        if (!errors.isEmpty()) {
            return assertErrorsContainer.add(new AssertErrorContainer(actualResponse).failed("CommonResponse verification failed:\n" + String.join("\n", errors)));
        }
        return assertErrorsContainer.add(new AssertErrorContainer(actualResponse).passed());
    }

    public static AssertErrorsContainer verifyCommonResponse(CommonResponseComparator expected, Response actualResponse) {

        logInfoMessage("=== Verify Response: {} ===", expected);
        logInfoMessage("Actual Result: {}", ResponseHelper.getBodyJsonString(actualResponse));
        AssertErrorsContainer assertErrorsContainer = new AssertErrorsContainer().add(verifyStatusCode(actualResponse));

        List<String> errors = new ArrayList<>();

        // Extract values from API response
        Number code = actualResponse.jsonPath().get("code");
        String type = actualResponse.jsonPath().getString("type");
        String message = actualResponse.jsonPath().getString("message");

        // Perform verifications
        verify("Code", expected.code(), code, errors);
        verify("Type", expected.type(), type, errors);
        verify("Message", expected.message(), message, errors);

        // If errors exist, fail with all mismatches
        if (!errors.isEmpty()) {
            return assertErrorsContainer.add(new AssertErrorContainer(actualResponse).failed("CommonResponse verification failed:\n" + String.join("\n", errors)));
        }
        return assertErrorsContainer.add(new AssertErrorContainer(actualResponse).passed());
    }

    private static void verify(String msg, IVerifier verifier, Object actual, List<String> errors) {
        if (verifier == null) {
            //Skip fields to verify
            return;
        }
        if (verifier.verify(actual)) {
            loggerInfoMsg(msg, verifier.getValue(), actual);
        } else {
            errors.add(msg +" mismatch: expected=" + verifier.getValue() + ", actual=" + actual);
        }
    }

    private static void verifyInt(String msg, int expected, int actual, List<String> errors) {
        if (actual == expected) {
            loggerInfoMsg(msg, expected, actual);
        } else {
            errors.add(msg +" mismatch: expected=" + expected + ", actual=" + actual);
        }
    }

    private static void verifyBool(String msg, boolean expected, boolean actual, List<String> errors) {
        if (actual == expected) {
            loggerInfoMsg(msg, expected, actual);
        } else {
            errors.add(msg +" mismatch: expected=" + expected + ", actual=" + actual);
        }
    }

    // for String, List<String>
    private static void verify(String msg, Object expected, Object actual, List<String> errors) {
        if (expected == null && actual == null) {
            loggerInfoMsg(msg, expected, actual);
            return;
        }
        if (expected == null) {
            errors.add(msg +" mismatch: expected=" + expected + ", actual=" + actual);
            return;
        }
        if (expected.equals(actual)) {
            loggerInfoMsg(msg, expected, actual);
        } else {
            errors.add(msg +" mismatch: expected=" + expected + ", actual=" + actual);
        }
    }

    private static void loggerInfoMsg(String msg, Object expected, Object actual) {
        logInfoMessage("PASSED: {}: expected={}, actual={}", msg, expected, actual);
    }

    private static void loggerInfoMsg(String msg, int expected, int actual) {
        logInfoMessage("PASSED: {}: expected={}, actual={}", msg, expected, actual);
    }

    public static AssertErrorContainer verifyStatusCode(Response response) {
        return verifyStatusCode(response, 200, "Status Code = 200 for response");
    }

    public static AssertErrorContainer verifyStatusCode(Callable<Response> response, int expectedCode, String message) {

        AtomicReference<Response> responseAtomic = new AtomicReference<>();
        Callable<Boolean> func = ()->{
            try {
                responseAtomic.set(response.call());
                verifyStatusCode(responseAtomic.get(), expectedCode, message);
            } catch (AssertionError e) {
                return false;
            }
            return true;
        };

        if (Util.waitUntilFuncTrue(func, TIMEOUT_MILLISEC_WAIT_RESPONSE)) {
            return new AssertErrorContainer(responseAtomic.get()).passed();
        }

        return verifyStatusCode(responseAtomic.get(), expectedCode, message);
    }

    public static AssertErrorContainer verifyStatusCode(Response response, int expectedCode, String message) {
        if (ResponseHelper.getStatusCode(response) == expectedCode) {
            logInfoMessage("PASSED: {}", message);
            return new AssertErrorContainer(response).passed();
        }
        return new AssertErrorContainer(response).failed(message).withActualResultInt(ResponseHelper.getStatusCode(response));
    }

    private static void logInfoMessage(String msg, Object... params) {
        logger.info(msg, params);
    }
}
