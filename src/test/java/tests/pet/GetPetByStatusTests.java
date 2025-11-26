package tests.pet;

import base.BaseTest;
import api.PetApi;
import api.ResponseHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import model.Category;
import model.Pet;
import model.PetStatus;
import model.Tag;
import org.testng.annotations.Test;

import java.util.List;

public class GetPetByStatusTests extends BaseTest {
    private final PetApi petApi = getPetApi();

    @Test
    public void testGetByStatus() throws JsonProcessingException {
        PetStatus petStatus = PetStatus.AVAILABLE;

        Pet pet = Pet.builder()
                .category(Category.createCategoryDogs())
                .name("Buddy")
                .photoUrls(List.of("https://example.com/photo1.jpg"))
                .tags(List.of(new Tag(1, "Nice"), new Tag(2, "Black")))
                .status(petStatus)
                .build();

        Response createResponse = petApi.create(pet);
        verifyStatusCode(createResponse, 200, "Create pet");

        Response response = petApi.getByStatus(petStatus);
        verifyStatusCode(response, 200, "Get pets by status "+ petStatus);

        verifyCount(response, ResponseHelper.getId(createResponse), 1, "Find pet by Status = " + petStatus);
    }
}
