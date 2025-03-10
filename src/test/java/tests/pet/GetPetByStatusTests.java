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

import java.util.Arrays;
import java.util.List;

public class GetPetByStatusTests extends BaseTest {
    private final PetApi petApi = getPetApi();

    @Test
    public void testGetByStatus() throws JsonProcessingException {
        PetStatus petStatus = PetStatus.AVAILABLE;

        List<String> photos = List.of("https://example.com/photo1.jpg");
        Pet pet = new Pet.Builder()
                .setCategory(Category.createCategoryDogs())
                .setName("Buddy")
                .setPhotoUrls(photos)
                .setTags(Arrays.asList(new Tag(1, "Nice"), new Tag(2, "Black")))
                .setStatus(petStatus)
                .build();

        Response createResponse = petApi.create(pet);
        verifyStatusCode(createResponse, 200, "Create pet");

        Response response = petApi.getByStatus(petStatus);
        verifyStatusCode(response, 200, "Get pets by status "+ petStatus);

        verifyCount(response, ResponseHelper.getId(createResponse), 1, "Find pet by Status = " + petStatus);
    }
}
