package uz.exadel.user.clients.session;

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
public class ShoppingSessionDto {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty(value = "total_amount")
    private BigDecimal totalAmount;
}