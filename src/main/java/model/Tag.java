package model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Tag {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
}
