package uz.exadel.product.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.exadel.product.entity.Category;
import uz.exadel.product.entity.Product;
import uz.exadel.product.exception.CategoryNotFoundException;
import uz.exadel.product.payload.ProductDto;
import uz.exadel.product.repo.CategoryRepo;

import javax.annotation.processing.Generated;
import java.util.Optional;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2021-03-11T19:21:44+0100",
        comments = "version: 1.4.2.Final, compiler: javac, environment: Java 13.0.2 (Oracle Corporation)"
)
@Component
@RequiredArgsConstructor
public class ProductMapperImpl implements CustomMapper<Product, ProductDto> {
    private final CategoryRepo categoryRepo;

    @Override
    public Product dtoToObject(ProductDto productDto) {
        if (productDto == null){
            return null;
        }

        Category category = getCategory(productDto.getCategoryId());

        Product product = Product.builder()
                .manufacturer(productDto.getManufacturer())
                .unit(productDto.getUnit())
                .name(productDto.getName())
                .description(productDto.getDescription())
                .SKU(productDto.getSKU())
                .category(category)
                .price(productDto.getPrice())
                .discount(productDto.getDiscount())
                .quantity(productDto.getQuantity())
                .build();

        return product;
    }

    @Override
    public ProductDto objectToDto(Product product) {
        if (product == null) return null;
        Category category = getCategory(product.getCategory().getId());

        ProductDto productDto = ProductDto.builder()
                .manufacturer(product.getManufacturer())
                .unit(product.getUnit())
                .name(product.getName())
                .description(product.getDescription())
                .SKU(product.getSKU())
                .categoryId(category.getId())
                .price(product.getPrice())
                .discount(product.getDiscount())
                .quantity(product.getQuantity())
                .build();
        return productDto;
    }

    @Override
    public Product dtoToObject(ProductDto productDto, Product product) {
        if (productDto == null) return null;

        Category category = getCategory(productDto.getCategoryId());


        Product buildProduct = product.builder()
                .discount(productDto.getDiscount())
                .price(productDto.getPrice())
                .category(category)
                .SKU(productDto.getSKU())
                .description(productDto.getDescription())
                .name(productDto.getName())
                .unit(product.getUnit())
                .manufacturer(productDto.getManufacturer())
                .createdAt(product.getCreatedAt())
                .quantity(product.getQuantity())
                .build();

        return buildProduct;
    }

    private Category getCategory(String categoryId){
        Optional<Category> byId = categoryRepo.findById(categoryId);
        if (byId.isEmpty()) throw new CategoryNotFoundException(String.format("Category with id %s is not found!", categoryId));
        Category category = byId.get();
        return category;
    }
}
