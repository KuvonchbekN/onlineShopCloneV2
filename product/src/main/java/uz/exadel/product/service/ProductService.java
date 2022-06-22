package uz.exadel.product.service;

import org.springframework.stereotype.Service;
import uz.exadel.clients.product.OrderItemDto;
import uz.exadel.product.entity.Product;
import uz.exadel.product.payload.ProductDto;

import java.util.List;

@Service
public interface ProductService extends BaseService<ProductDto, Product>{

    void checkIfThereIsEnoughProductInWarehouse(List<OrderItemDto> orderItemDtoList);
}
