package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.Pet;
import model.PetStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.CleanUpManager;

import java.io.File;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;

/**
 *  pet:
 *   - uploadImage
 *   - create pet
 *   - update pet
 *   - findByStatus
 *   - findById
 *   - update in store
 *   - delete pet
 */
public class PetApi extends BaseApi {

    private static final Logger logger = LogManager.getLogger(PetApi.class);

    private PetApi() {
        baseURI = Constants.BASE_URL_HTTPS;
    }

    public static PetApi getPetApi() {
        return new PetApi();
    }

    public Response create(Pet pet) throws JsonProcessingException {
        logger.info("--- Create Pet: {} ---", pet);

        Response response = getGiven()
                .contentType(JSON)
                .body(pet.asJson())
                .when()
                .post("/pet")
                .then()
                .extract().response();

        Object id = ResponseHelper.getId(response);
        logger.info("ID = {}", id);
        CleanUpManager.addPetId(id);

        return response;
    }

    public Response getById(Object petId) {
        logger.info("--- Get Pet by ID: {} ---", petId);
        return getGiven()
                .when()
                .get("/pet/" + petId)
                .then()
                .extract().response();
    }

    //https://petstore.swagger.io/v2/pet/findByStatus?status=available
    public Response getByStatus(PetStatus status) {
        logger.info("--- Get Pet by status: {} ---", status.getStatus());
        return getGiven()
                .when()
                .get("/pet/findByStatus?status=" + status.getStatus())
                .then()
                .extract().response();
    }

    public Response delete(Object petId) {
        logger.info("--- Delete Pet by ID: {} ---", petId);
        return getGiven()
                .when()
                .delete("/pet/" + petId)
                .then()
                .extract().response();
    }

    public Response update(Object petId, Pet newPet) throws JsonProcessingException {
        newPet.updateId(petId);
        logger.info("--- Update Pet by ID: {}, Pet: {} ---", petId, newPet);
        return getGiven()
                .contentType(JSON)
                .body(newPet.asJson())
                .when()
                .put("/pet")
                .then()
                .extract().response();
    }

    public Response updateInStore(Object petId, Pet pet, String name, PetStatus status) throws JsonProcessingException {
        pet.updateId(petId)
                .updateName(name)
                .updateStatus(status);
        logger.info("--- Update Pet in Store by ID: {}, Pet: {} ---", petId, pet);
        return getGiven()
                .contentType(ContentType.URLENC)
                .accept(ContentType.JSON)
                .formParam("name", pet.getName())
                .formParam("status", pet.getStatus().getStatus())
                .when()
                .post("/pet/"+ petId)
                .then()
                .extract().response();
    }

    public Response uploadImage(Object petId, File imageFile) {
        return getGiven()
                //.baseUri(Constants.BASE_URL_HTTPS)
                .contentType("multipart/form-data")
                .multiPart("file", imageFile)
                .when()
                .post("/pet/" + petId + "/uploadImage")
                .then()
                .extract()
                .response();
    }

}
