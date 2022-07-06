package uz.exadel.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.exadel.product.entity.Category;
import uz.exadel.product.payload.CategoryDto;
import uz.exadel.product.payload.ResponseItem;
import uz.exadel.product.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(new ResponseItem("Added Successfully",categoryService.create(categoryDto)));
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<Category> all = categoryService.getAll();
        return ResponseEntity.ok(new ResponseItem("All categories list", all));
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryById(@PathVariable String categoryId) {
        Category category = categoryService.getById(categoryId);
        return ResponseEntity.ok(new ResponseItem("Single category", category));
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable String categoryId, @RequestBody CategoryDto categoryDto) {
        String updatedCategoryId = categoryService.update(categoryDto, categoryId);
        return ResponseEntity.ok(new ResponseItem("Updated Category id", updatedCategoryId));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable String categoryId){
        categoryService.delete(categoryId);
        return ResponseEntity.ok(new ResponseItem("Deleted Successfully"));
    }
}
