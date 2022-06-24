package uz.exadel.order.clients.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

@FeignClient("product")
public interface ProductClient {

    @PostMapping("api/product/checkSufficiency")
    void isThereEnoughProductInWarehouse(@RequestBody Map<String, Integer> map);
}
