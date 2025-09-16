package api;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import io.restassured.response.Response;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ResponseHelper {

    public static long getIdLong(Response response) {
        Object id = getId(response);
        if (id == null) {
            return 0;
        }
        return (long)id;
    }

    public static Object getId(Response response) {
        if (getStatusCode(response) != 200) {
            return null;
        }
        Object id = response.body().jsonPath().get("id");
        return id == null ? 0 : id;
    }

    public static List<Object> getIds(Response response) {
        if (getStatusCode(response) != 200) {
            return null;
        }
        return response.body().jsonPath().getList("id");
    }

    public static Object getMessage(Response response) {
        if (getStatusCode(response) != 200) {
            return null;
        }
        return response.body().jsonPath().get("message");
    }

    public static int getStatusCode(Response response) {
        return response.getStatusCode();
    }

    public static String getBodyJsonString(Response response) {
        return response.body().jsonPath().get().toString();
    }

    public static int getCountResponsesById(Response response, @NotNull Object id) {
        return getListHashMapResponse(response, id).size();
    }

    public static List<LinkedHashMap<String, Object>> getListHashMapResponse(Response response, @NotNull Object id) {
        Number idNum = (Number) id;
        return ((List<LinkedHashMap<String, Object>>) response.body().jsonPath().get()).stream()
                .filter(map -> idNum.equals(map.get("id"))) // Ensure type-safe comparison
                .collect(Collectors.toList());
    }
}
