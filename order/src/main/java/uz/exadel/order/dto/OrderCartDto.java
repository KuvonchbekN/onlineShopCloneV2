package uz.exadel.order.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCartDto {
    @JsonProperty(value = "ordered_items")
    private List<OrderItemDto> orderedItemsDto = new ArrayList<>();

    @JsonProperty(value = "user_id")
    private String userId;
}
