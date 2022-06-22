package uz.exadel.product.mappers;

import org.springframework.stereotype.Component;
import uz.exadel.product.entity.Category;
import uz.exadel.product.payload.CategoryDto;

import javax.annotation.processing.Generated;

@Generated(
        value = "org.mapstruct.ap.MappingProcessor",
        date = "2021-03-11T19:21:44+0100",
        comments = "version: 1.4.2.Final, compiler: javac, environment: Java 13.0.2 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CustomMapper<Category, CategoryDto> {
    @Override
    public Category dtoToObject(CategoryDto categoryDto) {
        Category category = Category.builder()
                .description(categoryDto.getDescription())
                .name(categoryDto.getName())
                .build();
        return category;
    }

    @Override
    public CategoryDto objectToDto(Category category) {
        CategoryDto categoryDto = CategoryDto.builder()
                .description(category.getDescription())
                .name(category.getName())
                .build();
        return categoryDto;
    }

    @Override
    public Category dtoToObject(CategoryDto categoryDto, Category category) {
        Category updatedCategory = Category.builder()
                .description(categoryDto.getDescription())
                .name(categoryDto.getName())
                .createdAt(category.getCreatedAt())
                .id(category.getId())
                .products(category.getProducts())
                .build();
        return updatedCategory;
    }
}
