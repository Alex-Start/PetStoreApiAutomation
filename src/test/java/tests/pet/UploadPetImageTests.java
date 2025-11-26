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
import java.util.List;

public class UploadPetImageTests extends BaseTest {
    private final PetApi petApi = getPetApi();

    @Test
    public void testUploadPetImage() throws JsonProcessingException {
        Pet pet = Pet.builder()
                .category(Category.createCategoryCats())
                .name("Mur")
                .photoUrls(List.of("https://example.com/photo1.jpg"))
                .tags(List.of(new Tag(1, "Nice"), new Tag(2, "Black")))
                .status(PetStatus.AVAILABLE)
                .build();

        Response createResponse = petApi.create(pet);
        verifyStatusCode(createResponse, 200, "Create pet");

        File imageFile = new File("src/test/resources/cat.jpg");
        Response uploadResponse = petApi.uploadImage(ResponseHelper.getId(createResponse), imageFile);
        verifyStatusCode(uploadResponse, 200, "Upload pet image");
    }
}
