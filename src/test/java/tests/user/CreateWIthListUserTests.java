package tests.user;

import api.UserApi;
import base.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import model.User;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;


public class CreateWIthListUserTests extends BaseTest {
    private final UserApi userApi = getUserApi();

    @Test
    public void testCreateWithListUser() throws JsonProcessingException {
        User user1 = new User.Builder()
                .setUsername("test-1")
                .setFirstname("firstname-1")
                .setLastname("lastname-1")
                .setPassword("password-1")
                .setEmail("some-1@email.com")
                .setPhone("+123456")
                .setUserStatus(0)
                .build();
        User user2 = new User.Builder()
                .setUsername("test-2")
                .setFirstname("firstname-2")
                .setLastname("lastname-2")
                .setPassword("password-2")
                .setEmail("some-2@email.com")
                .setPhone("+456789")
                .setUserStatus(1)
                .build();

        List<User> userList = Arrays.asList(user1, user2);
        Response createResponse = userApi.createWithList(userList);
        verifyStatusCode(createResponse, 200, "Create users: "+ userList);

        for(User user : userList) {
            verify(() -> userApi.getByUsername(user.getUsername()), user);
        }
    }
}
