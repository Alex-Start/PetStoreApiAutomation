package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.CleanUpManager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.restassured.http.ContentType.JSON;

/**
 *  user:
 *   - create with list
 *   - get by name
 *   - update user
 *   - delete user
 *   - login
 *   - logout
 *   - createWithArray
 *   - create user
 */
public class UserApi extends BaseApi {
    private static final Logger logger = LogManager.getLogger(UserApi.class);

    private UserApi() {

    }

    public static UserApi getUserApi() {
        return new UserApi();
    }

    public Response create(User user) throws JsonProcessingException {
        logger.info("--- Create User: {} ---", user);

        Response response = getGiven()
                .contentType(JSON)
                .body(user.asJson())
                .when()
                .post("/user")
                .then()
                .extract().response();

        CleanUpManager.addUsername(user.getUsername());

        return response;
    }

    public Response createWithArray(User[] users) throws JsonProcessingException {
        return createMany(Arrays.asList(users), "/user/createWithArray");
    }

    public Response createWithList(List<User> users) throws JsonProcessingException {
        return createMany(users, "/user/createWithList");
    }

    private Response createMany(List<User> users, String uri) throws JsonProcessingException {
        logger.info("--- Create Users: {} ---", users);

        Response response = getGiven()
                .contentType(JSON)
                .body(User.asJson(users))
                .when()
                .post(uri)
                .then()
                .extract().response();

        CleanUpManager.addUsernames(users.stream().map(User::getUsername).collect(Collectors.toList()));

        return response;
    }

    public Response getByUsername(String username) {
        logger.info("--- Get User by username: {} ---", username);
        return getGiven()
                .when()
                .get("/user/" + username)
                .then()
                .extract().response();
    }

    //https://petstore.swagger.io/v2/user/login?username=xxx&password=xxx
    //Response body
    //{
    //  "code": 200,
    //  "type": "unknown",
    //  "message": "logged in user session:1741344892966"
    //}
    public Response login(String username, String password) {
        logger.info("--- Login User : {} ---", username);
        return getGiven()
                .when()
                .get("/user/login?username="+ username +"&password="+ password)
                .then()
                .extract().response();
    }

    public Response logout() {
        logger.info("--- Logout User ---");
        return getGiven()
                .when()
                .get("/user/logout")
                .then()
                .extract().response();
    }

    public Response delete(String username) {
        logger.info("--- Delete User by username: {} ---", username);
        return getGiven()
                .when()
                .delete("/user/" + username)
                .then()
                .extract().response();
    }

    public Response update(String username, Object id, User newUser) throws JsonProcessingException {
        newUser.updateId(id);
        logger.info("--- Update User by username: {}, User: {} ---", username, newUser);
        return getGiven()
                .contentType(JSON)
                .body(newUser.asJson())
                .when()
                .put("/user/" + username)
                .then()
                .extract().response();
    }
}
