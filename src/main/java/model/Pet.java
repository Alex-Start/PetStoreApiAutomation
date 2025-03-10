package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pet extends BaseModel implements IModel {
    @JsonProperty("id")
    private Number id; // int or long
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

    // Private Constructor
    private Pet(Builder builder) {
        this.id = builder.id;
        this.category = builder.category;
        this.name = builder.name;
        this.photoUrls = builder.photoUrls;
        this.tags = builder.tags;
        this.status = builder.status;
    }

    @Override
    public String toString() {
        return "Pet{" +
                (isZero(id) ? "" : " id=" + id + ", ") +
                "category=" + category +
                ", name='" + name + '\'' +
                ", photoUrls=" + photoUrls +
                ", tags=" + tags +
                ", status=" + status +
                '}';
    }

    // Builder Class
    public static class Builder {
        private Number id;
        private Category category;
        private String name;
        private List<String> photoUrls = new ArrayList<>();
        private List<Tag> tags = new ArrayList<>();
        private PetStatus status;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setCategory(Category category) {
            this.category = category;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setPhotoUrls(List<String> photoUrls) {
            if (photoUrls == null) {
                this.photoUrls = new ArrayList<>();
            } else {
                this.photoUrls = new ArrayList<>(photoUrls);
            }
            return this;
        }

        public Builder setTags(List<Tag> tags) {
            if (tags == null) {
                this.tags = new ArrayList<>();
            } else {
                this.tags = new ArrayList<>(tags);
            }
            return this;
        }

        public Builder setStatus(PetStatus status) {
            this.status = status;
            return this;
        }

        public Pet build() {
            return new Pet(this);
        }
    }

    public Object getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public PetStatus getStatus() {
        return status;
    }

    public Pet updateId(Object id) {
        this.id = (Number)id;
        return this;
    }

    public Pet updateCategory(Category newCategory) {
        category = newCategory;
        return this;
    }

    public Pet updateName(String newName) {
        name = newName;
        return this;
    }

    public Pet updatePhotoUrls(List<String> newPhotos) {
        photoUrls = newPhotos;
        return this;
    }

    public Pet updatePhotoUrls(String... newPhotos) {
        photoUrls.addAll(Arrays.asList(newPhotos));
        return this;
    }

    public Pet updateTags(List<Tag> newTags) {
        tags = newTags;
        return this;
    }

    public Pet updateTags(Tag... newTags) {
        tags.addAll(Arrays.asList(newTags));;
        return this;
    }

    public Pet updateStatus(PetStatus newStatus) {
        status = newStatus;
        return this;
    }

}
