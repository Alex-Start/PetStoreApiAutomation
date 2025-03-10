package api;

import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseApi {
    private final int TIMEOUT_CONNECTION = 3_000;
    private final int TIMEOUT_SOCKET = 3_000;

    protected RequestSpecification getGiven() {
        return given()
                .header(new Header("api_key", Constants.SPECIAL_KEY_AUTH))
                .config(RestAssuredConfig.newConfig().httpClient(
                        HttpClientConfig.httpClientConfig()
                                .setParam("http.connection.timeout", TIMEOUT_CONNECTION)  // Connection timeout (10 sec)
                                .setParam("http.socket.timeout", TIMEOUT_SOCKET)      // Response wait time (10 sec)
                ));
    }
}
