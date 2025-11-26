package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Pet extends BaseModel implements IModel {

    @JsonProperty("id")
    private Number id;

    @JsonProperty("category")
    private Category category;

    @JsonProperty("name")
    private String name;

    @JsonProperty("photoUrls")
    private List<String> photoUrls;

    @JsonProperty("tags")
    private List<Tag> tags;

    @JsonProperty("status")
    private PetStatus status;

    public Pet updateId(Object id) {
        this.id = (Number) id;
        return this;
    }

    public Pet updateCategory(Category newCategory) {
        this.category = newCategory;
        return this;
    }

    public Pet updateName(String newName) {
        this.name = newName;
        return this;
    }

    public Pet updatePhotoUrls(List<String> newPhotos) {
        this.photoUrls = newPhotos != null ? newPhotos : new ArrayList<>();
        return this;
    }

    public Pet updatePhotoUrls(String... newPhotos) {
        if (this.photoUrls == null) {
            this.photoUrls = new ArrayList<>();
        } else {
            this.photoUrls = new ArrayList<>(this.photoUrls);
        }
        this.photoUrls.addAll(Arrays.asList(newPhotos));
        return this;
    }

    public Pet updateTags(List<Tag> newTags) {
        this.tags = newTags != null ? newTags : new ArrayList<>();
        return this;
    }

    public Pet updateTags(Tag... newTags) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        } else {
            this.tags = new ArrayList<>(this.tags);
        }
        this.tags.addAll(Arrays.asList(newTags));
        return this;
    }

    public Pet updateStatus(PetStatus newStatus) {
        this.status = newStatus;
        return this;
    }
}
