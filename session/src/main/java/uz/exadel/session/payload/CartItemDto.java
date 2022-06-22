package uz.exadel.session.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private int quantity;

    @JsonProperty(value = "session_id")
    private String sessionId;

    @JsonProperty(value = "product_id")
    private String productId;
}
