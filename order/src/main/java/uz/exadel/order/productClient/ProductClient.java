package uz.exadel.order.productClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.exadel.order.dto.OrderItemDto;

import java.util.List;

@FeignClient("product")
public interface ProductClient {

    @PostMapping("api/product/checkSufficiency")
    void isThereEnoughProductInWarehouse(@RequestBody List<OrderItemDto> orderItemDtoList);
}
