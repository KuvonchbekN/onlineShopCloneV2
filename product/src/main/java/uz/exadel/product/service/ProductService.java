package uz.exadel.product.service;

import org.springframework.stereotype.Service;
import uz.exadel.product.entity.Product;
import uz.exadel.product.payload.ProductDto;

import java.util.List;
import java.util.Map;

@Service
public interface ProductService extends BaseService<ProductDto, Product>{

    void checkIfThereIsEnoughProductInWarehouse(Map<String, Integer> map);
}
