package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseModel {

    public String asJson() throws JsonProcessingException {
        return asJson(this);
    }

    public static String asJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    protected boolean isZero(Number id) {
        return id != null && id.longValue() == 0;
    }
}
