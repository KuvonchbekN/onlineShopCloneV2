package uz.exadel.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.product.entity.Category;
import uz.exadel.product.exception.CategoryAlreadyExistsException;
import uz.exadel.product.exception.CategoryNotFoundException;
import uz.exadel.product.mappers.CustomMapper;
import uz.exadel.product.payload.CategoryDto;
import uz.exadel.product.repo.CategoryRepo;
import uz.exadel.product.service.CategoryService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepo categoryRepo;
    private final CustomMapper<Category, CategoryDto> categoryMapper;
    @Override
    public List<Category> getAll() {
        return categoryRepo.findAll();
    }

    @Override
    public Category getById(String categoryId) {
        checkById(categoryId);
        Optional<Category> byId = categoryRepo.findById(categoryId);
        if (byId.isEmpty()){
            throw new CategoryNotFoundException(String.format("Category with id of %s is not found!", categoryId));
        }
        return byId.get();
    }

    @Override
    public String create(CategoryDto categoryDto) {
        checkByName(categoryDto.getName());

        Category category = categoryMapper.dtoToObject(categoryDto);
        Category savedCategory = categoryRepo.save(category);
        return savedCategory.getId();
    }

    @Override
    public String update(CategoryDto categoryDto, String categoryId) {
        checkById(categoryId);
        checkByName(categoryDto.getName());

        Category category = categoryMapper.dtoToObject(categoryDto);
        category.setId(categoryId);
        Category updatedCategory = categoryRepo.save(category);

        return updatedCategory.getId();
    }

    @Override
    public void delete(String id) {
        checkById(id);
        categoryRepo.deleteById(id);
    }

    private void checkByName(String name){
        if (categoryRepo.existsByName(name)){
            throw new CategoryAlreadyExistsException(String.format("Category with name %s already exists!", name));
        }
    }

    public void checkById(String categoryId){
        if (categoryId == null){
            throw new CategoryNotFoundException("The category id must not be null!");
        }
        boolean exists = categoryRepo.existsById(categoryId);
        if (!exists){
            throw new CategoryNotFoundException(String.format("Category with id %s is not found!", categoryId));
        }
    }
}
