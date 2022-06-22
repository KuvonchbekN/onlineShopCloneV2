package uz.exadel.product.payload;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Column;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductDto {
    @NotNull
    @Column(nullable = false)
    @JsonProperty(value = "name")
    private String name;

    private String description;

    private String manufacturer;

    private String unit;

    @Column(nullable = false)
    @JsonProperty(value = "sku")
    private String SKU; //each product has different sku number

    @Column(nullable = false)
    @NotNull
    private BigDecimal price;

    private int quantity;

    @JsonProperty(value = "discount")
    private int discount;

    @Column(nullable = false)
    @JsonProperty(value = "category_id")
    private String categoryId;
}
