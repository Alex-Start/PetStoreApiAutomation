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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class CreateDeletePetTests extends BaseTest {
    private final PetApi petApi = getPetApi();

    // Depends on implementation we can check pair wise combination, e.g.: pairwise.yuuniworks.com
    // But to simplify it and show main testing strategy we can use only a few combinations (see current data provider)
    // All combinations from pairwise.yuuniworks.com side are:
    //Category	Name	photos	tags	status
    //3	simpleName	ph1	null	Available
    //4	simpleName	null	tag1+2	Pending
    //null	number	null	tag1	null
    //1	simpleName	ph1+2	tag1	Sold
    //4	shortName	ph1+2	null	null
    //2	number	ph1+2	tag1+2	Available
    //2	simpleName	ph1	tag1	null
    //1	null	null	null	Pending
    //2	name with space	null	null	Sold
    //null	name with space	ph1	tag1+2	Pending
    //2	null	ph1+2	tag1	Available
    //3	name with space	ph1+2	tag1+2	null
    //1	shortName	ph1	tag1+2	Available
    //4	null	ph1	tag1+2	Sold
    //3	shortName	null	tag1	Pending
    //4	name with space	null	tag1	Available
    // null	simpleName	ph1+2	null	Available
    //3	number	ph1	null	Sold
    //2	shortName	ph1+2	tag1	Pending
    //null	shortName	null	tag1	Sold
    //1	number	null	null	null
    //4	number	ph1	tag1+2	Pending
    //null	null	null	tag1	null
    //1	name with space	null	tag1	Sold
    //3	null	ph1	null	null
    @DataProvider
    private Object[][] newPetParams() {
        // there are requirements for Pet name as example (we can extend here):
        String simpleName = "Buddy";
        String shortName = "e";
        String numberName = "1";
        String nameWithSpace = "Mr. dj";
        // there are requirements for photos and tags. TODO But we skip checking it to simplify.
        // photos and tags example:
        String ph1 = "https://side.com/photo1.jpg";
        String ph2 = "https://side.com/photo2.jpg";
        Tag tag1 = new Tag(1, "Nice");
        Tag tag2 = new Tag(2, "Black");
        return new Object [][] {
                {Category.createCategoryDogs()
                        , simpleName
                        , Arrays.asList(ph1, ph2)
                        , Arrays.asList(tag1)
                        , PetStatus.SOLD}
                ,
                {Category.createCategoryCats()
                        , numberName
                        , Arrays.asList(ph1, ph2)
                        , Arrays.asList(tag1, tag2)
                        , PetStatus.AVAILABLE}
                ,
                {Category.createCategoryRabbits()
                        , nameWithSpace
                        , Arrays.asList(ph1, ph2)
                        , Arrays.asList(tag1, tag2)
                        , null}
                ,
                {Category.createCategoryLions()
                        , null
                        , Arrays.asList(ph1)
                        , Arrays.asList(tag1, tag2)
                        , PetStatus.SOLD}
                ,
                {Category.createCategoryLions()
                        , simpleName
                        , Arrays.asList(ph1)
                        , null
                        , PetStatus.AVAILABLE}
                ,
                {Category.createCategoryLions()
                        , simpleName
                        , null
                        , Arrays.asList(tag1, tag2)
                        , PetStatus.PENDING}
                ,
                {null
                        , numberName
                        , null
                        , Arrays.asList(tag1)
                        , null}
                ,
                {Category.createCategoryLions()
                        , shortName
                        , Arrays.asList(ph1, ph2)
                        , null
                        , null}
        };
    }

    @Test(dataProvider = "newPetParams")
    public void testCreateVerifyDeletePet(Category category, String name, List<String> photos, List<Tag> tags, PetStatus status) throws JsonProcessingException {
        Pet pet = new Pet.Builder()
                .setCategory(category)
                .setName(name)
                .setPhotoUrls(photos)
                .setTags(tags)
                .setStatus(status)
                .build();

        Response createResponse = petApi.create(pet);
        verifyStatusCode(createResponse, 200, "Create pet");

        Object petId = ResponseHelper.getId(createResponse);

        // Simple Response:
        //Response getResponse = petApi.getById(petId);
        //PetVerifier.verifyPet(pet, getResponse);
        // Call response repeatedly:
        verify(pet, () -> petApi.getById(petId));

        verifyStatusCode(() -> petApi.delete(petId), 200, "Delete pet");

        verifyStatusCode(() -> petApi.getById(petId), 404, "Get deleted pet");
    }
}
