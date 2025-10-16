package base;

import api.PetApi;
import api.ResponseHelper;
import api.StoreApi;
import api.UserApi;
import io.restassured.response.Response;
import model.IModel;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Listeners;
import utils.CleanUpManager;
import listener.TestListener;
import verify.AssertErrorContainer;
import verify.AssertErrorsContainer;
import verify.VerifierResponse;

import java.util.concurrent.Callable;

@Listeners(TestListener.class)
public class BaseTest {

    PetApi petApi;
    UserApi userApi;
    StoreApi storeApi;

    protected PetApi getPetApi() {
        if (petApi == null) {
            petApi = api.PetApi.getPetApi();
        }
        return petApi;
    }

    protected UserApi getUserApi() {
        if (userApi == null) {
            userApi = api.UserApi.getUserApi();
        }
        return userApi;
    }

    protected StoreApi getStoreApi() {
        if (storeApi == null) {
            storeApi = api.StoreApi.getStoreApi();
        }
        return storeApi;
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        if (petApi != null) {
            CleanUpManager.getListPetId().forEach(petApi::delete);
            CleanUpManager.cleanPets();
        }
        if (userApi != null) {
            CleanUpManager.getListUsername().forEach(userApi::delete);
            CleanUpManager.cleanUsers();
        }
    }

    protected void verifyStatusCode(Response response, int expectedCode, String message) {
        AssertErrorContainer errorManager = VerifierResponse.verifyStatusCode(response, expectedCode, message);
        if (errorManager.isPassed()) {
            return;
        }

        Assert.assertEquals(errorManager.getActualResultInt(), expectedCode, errorManager.getErrorMessage()
                +":\nError Message: "+ ResponseHelper.getMessage(response));
    }

    protected void verifyStatusCode(Callable<Response> response, int expectedCode, String message) {
        AssertErrorContainer errorManager = VerifierResponse.verifyStatusCode(response, expectedCode, message);
        if (errorManager.isPassed()) {
            return;
        }

        Assert.assertEquals(errorManager.getActualResultInt(), expectedCode, errorManager.getErrorMessage()
                +":\nError Message: "+ ResponseHelper.getMessage(errorManager.getResponse()));
    }

    protected void verify(Response actualResponse, IModel expected) {
        AssertErrorsContainer errorManager = VerifierResponse.verify(expected, actualResponse);
        if (errorManager.isPassed()) {
            return;
        }

        Assert.fail(errorManager.getErrorMessage());
    }

    protected void verify(Callable<Response> actualResponse, IModel expected) {
        AssertErrorsContainer errorManager = VerifierResponse.verify(expected, actualResponse);
        if (errorManager.isPassed()) {
            return;
        }

        Assert.fail(errorManager.getErrorMessage());
    }

    protected void verifyCount(Response response, Object id, int count, String message) {
        int countById = ResponseHelper.getCountResponsesById(response, id);

        if (countById > count) {
            Assert.fail("There are many Pets with the same ID = " + id + ". Message: " + message);
        }
        Assert.assertEquals(countById, count, message);
    }

}