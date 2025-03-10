package tests.pet;

import base.BaseTest;
import api.PetApi;
import io.restassured.response.Response;
import model.PetStatus;
import org.testng.annotations.Test;

/**
 * Test for debug !
 */
public class PetTests extends BaseTest {

    private final PetApi petApi = getPetApi();

    // For debug!
    @Test
    public void deleteAll() {
        PetStatus petStatus = PetStatus.AVAILABLE;

        Response response = petApi.getByStatus(petStatus);
        verifyStatusCode(response, 200, "Get pets by status "+ petStatus);

        for(Object id : response.body().jsonPath().getList("id")) {
            System.out.println(petApi.delete(id).getStatusCode());
        }
    }

}
