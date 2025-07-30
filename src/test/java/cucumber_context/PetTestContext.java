package cucumber_context;

import io.restassured.response.Response;
import model.Pet;
import model.PetStatus;

import java.util.HashMap;
import java.util.Map;

public class PetTestContext {
    private Map<String, Response> response = new HashMap<>();
    private PetStatus petStatus;
    private Pet pet;

    private final String LAST_RESPONSE = "Last Response";

    public static final String DELIM_LIST = ", ";
    public static final String DELIM_OBJECT = "/";


    public void cleanUpAllResponses() {
        response.clear();
    }

    public Response getResponse() {
        return response.get(LAST_RESPONSE);
    }

    public void setResponse(Response response) {
        this.response.put(LAST_RESPONSE, response);
    }

    public Response getCreatePetResponse() {
        return response.get("Create pet");
    }

    public void setCreatePetResponse(Response response) {
        setResponse("Create pet", response);
        setResponse(response);
    }

    public Response getUpdatePetResponse() {
        return response.get("Update pet");
    }

    public void setUpdatePetResponse(Response response) {
        setResponse("Update pet", response);
        setResponse(response);
    }

    public Response getStatusPetResponse() {
        return response.get("Status pet");
    }

    public void setStatusPetResponse(Response response) {
        setResponse("Status pet", response);
        setResponse(response);
    }

    private void setResponse(String name, Response response) {
        this.response.put(name, response);
    }

    public PetStatus getPetStatus() {
        return petStatus;
    }

    public void setPetStatus(PetStatus petStatus) {
        this.petStatus = petStatus;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
