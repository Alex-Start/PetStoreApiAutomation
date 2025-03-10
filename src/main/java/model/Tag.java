package model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Tag {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;

    public Tag(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
