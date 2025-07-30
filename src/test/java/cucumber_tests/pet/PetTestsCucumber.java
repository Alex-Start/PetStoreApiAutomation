package cucumber_tests.pet;

import api.PetApi;
import api.ResponseHelper;
import base.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import cucumber_context.PetTestContext;
import io.cucumber.java.Before;
import io.cucumber.java.ParameterType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import model.Category;
import model.Pet;
import model.PetStatus;
import model.Tag;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PetTestsCucumber extends BaseTest {
    private final PetApi petApi = getPetApi();

    private final PetTestContext context;

    public PetTestsCucumber(PetTestContext context) {
        this.context = context;
    }

    @Before
    public void cleanUp() {
        context.cleanUpAllResponses();
        context.setPetStatus(null);;
        context.setPet(null);
    }

    @ParameterType(".*")//AVAILABLE|SOLD|PENDING
    public PetStatus petStatus(String statusName) {
        statusName = convertPlaceholder(statusName);
        if (statusName == null) {
            return null;
        }
        return PetStatus.valueOf(statusName);
    }

    private String convertPlaceholder(String value) {
        switch (value) {
            case "EMPTY" -> {
                return "";
            }
            case "NULL" -> {
                return null;
            }
            default -> {
                return value;
            }
        }
    }

    private List<String> listOfItems(String items) {
        return listOfItems(items, PetTestContext.DELIM_LIST);
    }

    private List<String> listOfItems(String items, String delim) {
        return Arrays.asList(items.split(delim));
    }

    @ParameterType(".*")
    public Category category(String category) {
        switch (category) {//TODO case sensitive
            case "dogs" -> {
                return Category.createCategoryDogs();
            }
            case "cats" -> {
                return Category.createCategoryCats();
            }
            case "lions" -> {
                return Category.createCategoryLions();
            }
            case "rabbits" -> {
                return Category.createCategoryRabbits();
            }
            case "NULL" -> {
                return null;
            }
            default -> throw new IllegalArgumentException("Incorrect category value: "+ category);
        }
    }

    @ParameterType(".*")
    public String name(String name) {
        return name;
    }

    @ParameterType(".*")
    public List<String> photos(String strList) {
        strList = convertPlaceholder(strList);
        if (strList == null) {
            return null;
        }
        return listOfItems(strList);
    }

    @ParameterType(".*")
    public List<Tag> tags(String strList) {
        strList = convertPlaceholder(strList);
        if (strList == null) {
            return null;
        }
        if (strList.isEmpty()) {
            throw new IllegalArgumentException("Tag string value is empty");
        }
        List<String> listTagsStringParams = listOfItems(strList, PetTestContext.DELIM_OBJECT);
        return listTagsStringParams.stream().map(x->{
            List<String> params = listOfItems(x);
            return new Tag(Integer.parseInt(params.get(0)), params.get(1));
        }).toList();
    }

    @Given("get all pets with status = '{petStatus}'")
    public void getAllPetsForStatus(PetStatus petStatus) {
        context.setPetStatus(petStatus);
        context.setCreatePetResponse(petApi.getByStatus(petStatus));
    }
    @Then("returns {int} and log message: '{string}'")
    public void returns(Integer status, String message) {
        verifyStatusCode(context.getResponse(), status, message);
    }
    @When("delete pets")
    public void deletePets() {
        for(Object id : context.getResponse().body().jsonPath().getList("id")) {
            System.out.println(petApi.delete(id).getStatusCode());
        }
    }

    @When("create pet with params category='{category}', name='{name}', photos='{photos}', tags='{tags}', status='{petStatus}'")
    public void createPetWithParamsCategoryNamePhotosTagsStatusValues(Category category, String name, List<String> photos, List<Tag> tags, PetStatus petStatus) throws JsonProcessingException {
        createPetWithParamsCategoryNamePhotosTagsStatus(category, name, photos, tags, petStatus);
    }

    @When("create pet with params '{category}', '{name}', '{photos}', '{tags}', '{petStatus}'")
    public void createPetWithParamsCategoryNamePhotosTagsStatus(Category category, String name, List<String> photos, List<Tag> tags, PetStatus status) throws JsonProcessingException {
        context.setPet(new Pet.Builder()
                .setCategory(category)
                .setName(name)
                .setPhotoUrls(photos)
                .setTags(tags)
                .setStatus(status)
                .build());
        context.setCreatePetResponse(petApi.create(context.getPet()));
    }

    @Then("verify pet by ID")//get last response and verify with pet
    public void verifyPetByID() {
        Object petId = ResponseHelper.getId(context.getResponse());
        verify(context.getPet(), () -> petApi.getById(petId));
    }

    @Then("delete pet and verify it passed")
    public void deletePetAndVerifyItPassed() {
        Object petId = ResponseHelper.getId(context.getResponse());
        verifyStatusCode(() -> petApi.delete(petId), 200, "Delete pet");
    }

    @Then("verify that not found deleted pet")
    public void verifyNotFoundDeletedPet() {
        Object petId = ResponseHelper.getId(context.getResponse());
        verifyStatusCode(() -> petApi.getById(petId), 404, "Get deleted pet");
    }

    @When("get pet by status='{petStatus}'")
    public void getPetByStatusAVAILABLE(PetStatus petStatus) {
        context.setPetStatus(petStatus);
        Response response = petApi.getByStatus(petStatus);
        System.out.println(ResponseHelper.getBodyJsonString(response));
        context.setStatusPetResponse(response);
    }

    @Then("verify count responses")
    public void verifyCountResponses() {
        verifyCount(context.getStatusPetResponse(), ResponseHelper.getId(context.getCreatePetResponse()), 1, "Find pet by Status = " + context.getPetStatus());
    }

    @When("update pet with new params category='{category}', name='{name}', photos='{photos}', tags='{tags}', status='{petStatus}'")
    public void updatePetWithNewParamsCategoryLionsNameNewNamePhotosHttpsExampleComPhotoJpgTagsStatusPENDING(Category category, String name, List<String> photos, List<Tag> tags, PetStatus petStatus) throws JsonProcessingException {
        context.getPet()
                .updateName("new name")
                .updateCategory(Category.createCategoryLions())
                .updatePhotoUrls("https://example.com/photo2.jpg")
                .updateTags(new Tag(3, "Small"))
                .updateStatus(PetStatus.PENDING);

        context.setUpdatePetResponse(petApi.update(ResponseHelper.getId(context.getCreatePetResponse()), context.getPet()));
    }

    @When("update pet in store with params name='{name}', status='{petStatus}'")
    public void updatePetInStoreWithParamsNameStatusValue(String name, PetStatus status) throws JsonProcessingException {
        Response updateResponse = petApi.updateInStore(ResponseHelper.getId(context.getResponse()), context.getPet(), name, status);

        context.setUpdatePetResponse(updateResponse);
    }

    @Then("verify pet by ID from the first created pet")
    public void verifyPetByIDFromTheFirstCreatedPet() {
        Object petId = ResponseHelper.getId(context.getCreatePetResponse());
        System.out.println("Find pet by ID: "+ petId);
        verify(context.getPet(), () -> petApi.getById(petId));
    }

    @When("upload image '{string}'")
    public void uploadImage(String pathImage) {
        File imageFile = new File(pathImage);
        context.setUpdatePetResponse(petApi.uploadImage(ResponseHelper.getId(context.getResponse()), imageFile));
    }
}
