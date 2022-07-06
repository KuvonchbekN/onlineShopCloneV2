package uz.exadel.product.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.product.entity.Category;
import uz.exadel.product.entity.Product;
import uz.exadel.product.exception.CategoryNotFoundException;
import uz.exadel.product.mappers.CategoryMapperImpl;
import uz.exadel.product.mappers.CustomMapper;
import uz.exadel.product.payload.CategoryDto;
import uz.exadel.product.repo.CategoryRepo;
import uz.exadel.product.service.CategoryService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

//@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Transactional
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepo categoryRepo;


    private CustomMapper<Category, CategoryDto> categoryMapper = new CategoryMapperImpl();
    @InjectMocks
    private CategoryService categoryService = new CategoryServiceImpl(categoryRepo, categoryMapper);

    private Category category;

    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        CustomMapper<Category, CategoryDto> customMapper = new CategoryMapperImpl();
        categoryService = new CategoryServiceImpl(categoryRepo, customMapper);
        product = new Product("1", "aaa", "aaa", "aaa", "aaa", "aaa", 10, category, BigDecimal.valueOf(10), 10, Timestamp.valueOf(LocalDateTime.now()));
        category = new Category("1", "aaa", "aaa", null, Timestamp.valueOf(LocalDateTime.now()));
    }


    @Test
    public void addNewCategory() {
        Category categoryToBeSaved = new Category(null, "something", "something really nice:)", null, Timestamp.valueOf(LocalDateTime.now()));
        Category afterBeingSaved = new Category("cat1", "something", "something really nice:)", null, Timestamp.valueOf(LocalDateTime.now()));
        lenient().when(categoryRepo.save(any(Category.class))).thenReturn(afterBeingSaved);  //use any(Category.class)

        CategoryDto categoryDto = categoryMapper.objectToDto(categoryToBeSaved);

        String categoryId = categoryService.create(categoryDto);
        assertEquals(categoryId, "cat1");
    }

    @Test
    public void checkById_idNullGiven() {
        String categoryId = null;

        CategoryNotFoundException categoryNotFoundException = assertThrows(CategoryNotFoundException.class, () -> categoryService.checkById(categoryId));
        assertTrue(categoryNotFoundException.getMessage().contains("must not be null"));
    }

    @Test
    public void checkById_cannotFindCategoryWithGivenId() {
        when(categoryRepo.existsById("someId")).thenReturn(false);
        CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class,
                () -> categoryService.checkById("someId"));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    public void deleteById_success(){
        String id = "cat1";

        willDoNothing().given(categoryRepo).deleteById(id);
        when(categoryRepo.existsById("cat1")).thenReturn(true);

        categoryService.delete(id);

        verify(categoryRepo, times(1)).deleteById(id);
    }

    @Test
    public void update_success(){
        Category categoryToBeSaved = new Category(null, "something", "something really nice:)", null, Timestamp.valueOf(LocalDateTime.now()));
        Category afterBeingSaved = new Category("cat1", "something", "something really nice:)", null, Timestamp.valueOf(LocalDateTime.now()));
        lenient().when(categoryRepo.save(any(Category.class))).thenReturn(afterBeingSaved);  //use any(Category.class)
        when(categoryRepo.existsById("cat1")).thenReturn(true);



        CategoryDto categoryDto = CategoryDto.builder()
                .name(categoryToBeSaved.getName())
                .description(categoryToBeSaved.getDescription())
                .build();

        String updateId = categoryService.update(categoryDto, afterBeingSaved.getId());

        assertEquals(updateId, "cat1");
    }

    @Test
    public void getAll_success(){
        when(categoryRepo.findAll()).thenReturn(List.of(category));

        List<Category> all = categoryService.getAll();
        assertEquals(all.size(), 1);
    }

    @Test
    public void getById_success(){
        when(categoryRepo.existsById("1")).thenReturn(true);
        when(categoryRepo.findById("1")).thenReturn(Optional.of(category));

        Category byId = categoryService.getById("1");

        assertEquals(byId.getId(), "1");
    }
}
















