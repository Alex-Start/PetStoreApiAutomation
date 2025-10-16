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

public class UpdatePetTests extends BaseTest {
    private final PetApi petApi = getPetApi();

    @Test
    public void testUpdatePet() throws JsonProcessingException {
        Pet pet = new Pet.Builder()
                .setCategory(Category.createCategoryDogs())
                .setName("Buddy")
                .setPhotoUrls(List.of("https://example.com/photo1.jpg"))
                .setTags(List.of(new Tag(1, "Nice"), new Tag(2, "Black")))
                .setStatus(PetStatus.AVAILABLE)
                .build();

        Response createResponse = petApi.create(pet);
        verifyStatusCode(createResponse, 200, "Create pet");

        verify(() -> petApi.getById(ResponseHelper.getId(createResponse)), pet);

        pet
                .updateName("new name")
                .updateCategory(Category.createCategoryLions())
                .updatePhotoUrls("https://example.com/photo2.jpg")
                .updateTags(new Tag(3, "Small"))
                .updateStatus(PetStatus.PENDING);

        Response updateResponse = petApi.update(ResponseHelper.getId(createResponse), pet);
        verifyStatusCode(updateResponse, 200, "Update pet");

        Response getResponse = petApi.getById(ResponseHelper.getId(createResponse));
        verify(getResponse, pet);
    }
}
