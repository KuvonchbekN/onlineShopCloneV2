package uz.exadel.clients.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("product")
public interface ProductClient {

    @PostMapping("api/product/checkSufficiency")
    void isThereEnoughProductInWarehouse(@RequestBody List<OrderItemDto> orderItemDtoList);
}
