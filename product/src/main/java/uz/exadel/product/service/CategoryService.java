package uz.exadel.product.service;

import org.springframework.stereotype.Service;
import uz.exadel.product.entity.Category;
import uz.exadel.product.payload.CategoryDto;

@Service
public interface CategoryService extends BaseService<CategoryDto, Category> {
}
