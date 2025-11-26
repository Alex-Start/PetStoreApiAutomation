package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

// https://github.com/swagger-api/swagger-petstore/blob/master/src/main/java/io/swagger/petstore/data/PetData.java
//        categories.add(createCategory(1, "Dogs"));
//        categories.add(createCategory(2, "Cats"));
//        categories.add(createCategory(3, "Rabbits"));
//        categories.add(createCategory(4, "Lions"));
@Getter
@NoArgsConstructor
@ToString
public class Category {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;

    public Category(int id, String name) {
        if (id < 0) {
            throw new IllegalArgumentException("Incorrect Category id: "+ id);
        }
        this.id = id;
        this.name = name;
    }

    public static Category createCategoryDogs() {
        return new Category(1, "Dogs");
    }
    public static Category createCategoryCats() {
        return new Category(2, "Cats");
    }
    public static Category createCategoryRabbits() {
        return new Category(3, "Rabbits");
    }
    public static Category createCategoryLions() {
        return new Category(4, "Lions");
    }
}
