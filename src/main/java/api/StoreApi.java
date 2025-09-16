package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import model.Order;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utils.CleanUpManager;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.http.ContentType.JSON;

/**
 *  store:
 *   - inventory
 *   - new order
 *   - find by orderId
 *   - delete by OrderId
 */
public class StoreApi extends BaseApi {

    private static final Logger logger = LogManager.getLogger(StoreApi.class);

    private StoreApi() {

    }

    public static StoreApi getStoreApi() {
        return new StoreApi();
    }

    public Response inventory() {
        logger.info("--- Store Inventory ---");

        Response response = getGiven()
                .when()
                .get("/store/inventory")
                .then()
                .extract().response();

        return response;
    }

    public Response create(Order order) throws JsonProcessingException {
        logger.info("--- Create Order: {} ---", order);

        Response response = getGiven()
                .contentType(JSON)
                .body(order.asJson())
                .when()
                .post("/store/order")
                .then()
                .extract().response();

        CleanUpManager.addOrder(ResponseHelper.getId(response));

        return response;
    }

    public Response getById(Object orderId) {
        logger.info("--- Get Order by ID: {} ---", orderId);
        return getGiven()
                .when()
                .get("/store/order/" + orderId)
                .then()
                .extract().response();
    }

    public Response delete(Object orderId) {
        logger.info("--- Delete Order by ID: {} ---", orderId);
        return getGiven()
                .when()
                .delete("/store/order/" + orderId)
                .then()
                .extract().response();
    }
}
