package tests.store;

import api.PetApi;
import api.ResponseHelper;
import api.StoreApi;
import base.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import model.*;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;


public class CreateDeleteOrderTests extends BaseTest {
    private final StoreApi storeApi = getStoreApi();
    private final PetApi petApi = getPetApi();

    @Test
    public void testCreateVerifyDelete() throws JsonProcessingException {
        List<String> photos = List.of("https://example.com/photo1.jpg");
        Pet pet = Pet.builder()
                .category(Category.createCategoryDogs())
                .name("Buddy")
                .photoUrls(photos)
                .tags(Arrays.asList(new Tag(1, "Nice"), new Tag(2, "Black")))
                .status(PetStatus.AVAILABLE)
                .build();

        Response petResponse = petApi.create(pet);
        verifyStatusCode(petResponse, 200, "Create pet");

        Order order = Order.builder()
                .petId(ResponseHelper.getIdLong(petResponse))
                .quantity(2)
                .shipDate()
                .status("new")
                .complete(false)
                .build();

        Response createResponse = storeApi.create(order);
        verifyStatusCode(createResponse, 200, "Create order");

        verify(() -> storeApi.getById(ResponseHelper.getId(createResponse)), order);

        verifyStatusCode(() -> storeApi.delete(ResponseHelper.getId(createResponse)), 200, "Delete order");

        verifyStatusCode(() -> storeApi.getById(ResponseHelper.getId(createResponse)), 404, "Get deleted order");
    }
}
