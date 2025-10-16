package tests.user;

import api.UserApi;
import base.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import model.User;
import org.testng.annotations.Test;

public class CreateDeleteUserTests extends BaseTest {
    private final UserApi userApi = getUserApi();

    @Test
    public void testCreateVerifyDeleteUser() throws JsonProcessingException {
        User user = new User.Builder()
                .setUsername("test1")
                .setFirstname("firstname1")
                .setLastname("lastname1")
                .setPassword("password1")
                .setEmail("some@email.com")
                .setPhone("+123456")
                .setUserStatus(0)
                .build();

        Response createResponse = userApi.create(user);
        verifyStatusCode(createResponse, 200, "Create user");

        verify(() -> userApi.getByUsername(user.getUsername()), user);

        verifyStatusCode(() -> userApi.delete(user.getUsername()), 200, "Delete user");

        verifyStatusCode(() -> userApi.getByUsername(user.getUsername()), 404, "Get deleted user");
    }
}
