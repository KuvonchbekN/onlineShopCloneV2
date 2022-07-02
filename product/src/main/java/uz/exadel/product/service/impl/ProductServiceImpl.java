package uz.exadel.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.clients.product.OrderItemDto;
import uz.exadel.product.entity.Product;
import uz.exadel.product.exception.ProductAlreadyExistsException;
import uz.exadel.product.exception.ProductNotFoundException;
import uz.exadel.product.exception.UnsufficientProductException;
import uz.exadel.product.mappers.CustomMapper;
import uz.exadel.product.payload.ProductDto;
import uz.exadel.product.repo.ProductRepo;
import uz.exadel.product.service.CategoryService;
import uz.exadel.product.service.ProductService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final CategoryService categoryService;
    private final CustomMapper<Product, ProductDto> productMapper;


    @Override
    public List<Product> getAll() {
        return productRepo.findAll();
    }

    @Override
    public Product getById(String productId) {
        Optional<Product> byId = productRepo.findById(productId);
        if (byId.isEmpty()) {
            throw new ProductNotFoundException("Product not found!");
        }
        return byId.get();
    }

    @Override
    public String create(ProductDto productDto) {
        checkBySku(productDto.getSKU()); //checks by product sku
        categoryService.getById(productDto.getCategoryId()); //checks by category id
        Product product = productMapper.dtoToObject(productDto);
        Product savedProduct = productRepo.save(product);
        return savedProduct.getId();
    }

    @Override
    public String update(ProductDto productDto, String productId) {
        checkBySku(productDto.getSKU()); //checks by product sku
        checkById(productId);
        categoryService.checkById(productDto.getCategoryId());//checks by category id
        Product product = productMapper.dtoToObject(productDto);
        product.setId(productId);
        Product updatedProduct = productRepo.save(product);
        return updatedProduct.getId();
    }

    @Override
    public void delete(String productId) {
        checkById(productId);
        productRepo.deleteById(productId);
    }

    private void checkBySku(String sku) {
        boolean exists = productRepo.existsBySKU(sku);
        if (exists) {
            throw new ProductAlreadyExistsException(String.format("Product with SKU %s already exists!", sku));
        }
    }

    public void checkById(String productId) {
        if (productId == null) {
            throw new ProductNotFoundException("The product id should not be null!");
        }
        boolean exists = productRepo.existsById(productId);
        if (!exists) {
            throw new ProductNotFoundException(String.format("Product with id %s is not found!", productId));
        }
    }

    @Override
    public void checkIfThereIsEnoughProductInWarehouse(List<OrderItemDto> orderItemDtoList) {
        for (OrderItemDto orderItemDto : orderItemDtoList) {
            int requestedProductQuantity = orderItemDto.getProductQuantity();
            int foundQuantity = productRepo.findProductInformationByProductId(orderItemDto.getProductId());

            if (requestedProductQuantity > foundQuantity) {
                String productName = productRepo.findNameById(orderItemDto.getProductId());
                throw new UnsufficientProductException(String.format("There is not requested amount of %s in the warehouse, you can buy at most %s %s currently", productName, foundQuantity, productName));
            }
        }
        boughtProduct(orderItemDtoList); //this method updates the product table;
    }

    public void boughtProduct(List<OrderItemDto> orderItemDtoList) {
        orderItemDtoList.forEach(orderItemDto -> {
            Optional<Product> byId = productRepo.findById(orderItemDto.getProductId());
            if (byId.isEmpty()) {
                throw new ProductNotFoundException("Product with this id is not found");
            }

            Product product = byId.get();
            product.setQuantity(product.getQuantity() - orderItemDto.getProductQuantity());
        });
    }
}
