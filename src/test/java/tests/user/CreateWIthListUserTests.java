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
        User user1 = User.builder()
                .username("test-1")
                .firstname("firstname-1")
                .lastname("lastname-1")
                .password("password-1")
                .email("some-1@email.com")
                .phone("+123456")
                .userStatus(0)
                .build();
        User user2 = User.builder()
                .username("test-2")
                .firstname("firstname-2")
                .lastname("lastname-2")
                .password("password-2")
                .email("some-2@email.com")
                .phone("+456789")
                .userStatus(1)
                .build();

        List<User> userList = Arrays.asList(user1, user2);
        Response createResponse = userApi.createWithList(userList);
        verifyStatusCode(createResponse, 200, "Create users: "+ userList);

        for(User user : userList) {
            verify(() -> userApi.getByUsername(user.getUsername()), user);
        }
    }
}
