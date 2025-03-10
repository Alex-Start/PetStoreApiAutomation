package tests.pet;

import api.PetApi;
import api.ResponseHelper;
import base.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import model.Category;
import model.Pet;
import model.PetStatus;
import model.Tag;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class UpdatePetInStoreTests extends BaseTest {
    private final PetApi petApi = getPetApi();

    @Test
    public void testUpdatePetInStore() throws JsonProcessingException {
        List<String> photos = List.of("https://example.com/photo1.jpg");
        Pet pet = new Pet.Builder()
                .setCategory(Category.createCategoryDogs())
                .setName("Buddy")
                .setPhotoUrls(photos)
                .setTags(Arrays.asList(new Tag(1, "Nice"), new Tag(2, "Black")))
                .setStatus(PetStatus.AVAILABLE)
                .build();

        Response createResponse = petApi.create(pet);
        verifyStatusCode(createResponse, 200, "Create pet");

        verify(pet, () -> petApi.getById(ResponseHelper.getId(createResponse)));

        Response updateResponse = petApi.updateInStore(ResponseHelper.getId(createResponse), pet, "new name", PetStatus.SOLD);
        verifyStatusCode(updateResponse, 200, "Update pet");

        verify(pet, () -> petApi.getById(ResponseHelper.getId(createResponse)));
    }
}
