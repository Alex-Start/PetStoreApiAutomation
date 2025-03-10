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

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class UploadPetImageTests extends BaseTest {
    private final PetApi petApi = getPetApi();

    @Test
    public void testUploadPetImage() throws JsonProcessingException {
        List<String> photos = List.of("https://example.com/photo1.jpg");
        Pet pet = new Pet.Builder()
                .setCategory(Category.createCategoryCats())
                .setName("Mur")
                .setPhotoUrls(photos)
                .setTags(Arrays.asList(new Tag(1, "Nice"), new Tag(2, "Black")))
                .setStatus(PetStatus.AVAILABLE)
                .build();

        Response createResponse = petApi.create(pet);
        verifyStatusCode(createResponse, 200, "Create pet");

        File imageFile = new File("src/test/resources/cat.jpg");
        Response uploadResponse = petApi.uploadImage(ResponseHelper.getId(createResponse), imageFile);
        verifyStatusCode(uploadResponse, 200, "Upload pet image");
    }
}
