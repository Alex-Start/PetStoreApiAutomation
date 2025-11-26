package tests.user;

import api.UserApi;
import base.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import model.CommonResponseComparator;
import model.User;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import static verify.NumberVerifier.asNumber;
import static verify.RegExpVerifier.asRegexp;
import static verify.StringVerifier.asString;

public class LoginLogoutUserTests extends BaseTest {
    private final UserApi userApi = getUserApi();

    @Test
    public void testLoginLogoutUser() throws JsonProcessingException {
        User user = User.builder()
                .username("test"+ DateTime.now())
                .firstname("firstname1")
                .lastname("lastname1")
                .password("password1")
                .email("some@email.com")
                .phone("+123456")
                .userStatus(0)
                .build();

        Response createResponse = userApi.create(user);
        verifyStatusCode(createResponse, 200, "Create user: "+ user.getUsername());

        Response loginResponse = userApi.login(user.getUsername(), user.getPassword());
        verify(loginResponse, new CommonResponseComparator(asNumber(200)
                , asString("unknown")
                , asRegexp("logged in user session:\\d*")));

        Response logoutResponse = userApi.logout();
        verify(logoutResponse, new CommonResponseComparator(asNumber(200)
                , asString("unknown")
                , asString("ok")));
    }
}
