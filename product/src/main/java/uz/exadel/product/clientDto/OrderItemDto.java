package uz.exadel.product.clientDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("quantity")
    private int productQuantity;

    private BigDecimal price;
}
