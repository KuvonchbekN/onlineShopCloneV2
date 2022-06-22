package uz.exadel.product.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Column;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    @NotNull
    @Column(nullable = false)
    @JsonProperty(value = "name")
    private String name;

    private String description;
}
