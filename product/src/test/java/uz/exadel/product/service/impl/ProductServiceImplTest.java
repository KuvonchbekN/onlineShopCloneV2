package uz.exadel.product.service.impl;

import org.antlr.stringtemplate.language.Cat;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.product.entity.Category;
import uz.exadel.product.entity.Product;
import uz.exadel.product.exception.ProductNotFoundException;
import uz.exadel.product.mappers.CategoryMapperImpl;
import uz.exadel.product.mappers.CustomMapper;
import uz.exadel.product.mappers.ProductMapperImpl;
import uz.exadel.product.payload.CategoryDto;
import uz.exadel.product.payload.ProductDto;
import uz.exadel.product.repo.CategoryRepo;
import uz.exadel.product.repo.ProductRepo;
import uz.exadel.product.service.CategoryService;
import uz.exadel.product.service.ProductService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

//@SpringBootTest => this is for integration testing
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Transactional
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

//    @Mock
//    private CategoryRepo categoryRepo;

//    @Autowired
//    private CustomMapper<Category, CategoryDto> categoryMapper = new CategoryMapperImpl();

    @MockBean(name = "categoryServiceImpl")
    private CategoryServiceImpl categoryService ;

    @Mock(name = "productRepo")
    private ProductRepo productRepo;
    @InjectMocks
    private ProductServiceImpl productService;


    @Mock
    private ProductMapperImpl productMapper;




    private Product product;

    @BeforeEach
    void setUp() {
        Category category = new Category("1", "aaa", "aaa", null, Timestamp.valueOf(LocalDateTime.now()));
        product = new Product("1", "aaa", "aaa", "aaa", "aaa", "aaa", 10, category, BigDecimal.valueOf(10), 10, Timestamp.valueOf(LocalDateTime.now()));
    }

    @DisplayName(value = "Adding New Product")
    @Test
     void create() {
        lenient().when(productRepo.save(any(Product.class))).thenReturn(product);
        ProductDto productDto = new ProductDto(product.getName(), product.getDescription(),
                product.getManufacturer(), product.getUnit(), product.getSKU(), product.getPrice(),
                product.getQuantity(), product.getDiscount(),product.getCategory().getId());
        String id = productService.create(productDto);

        assertEquals(id, "1");
    }

    @Test
    void getAll() {
        when(productRepo.findAll()).thenReturn(new ArrayList<>(List.of(product)));
        assertEquals(productService.getAll().size(), 1);
    }

    @Test
    void getByIdWithWrongProductId() {
        ProductNotFoundException productNotFoundException = assertThrows(ProductNotFoundException.class, () -> productService.checkById(null));
        assertTrue(productNotFoundException.getMessage().contains("should not be null"));
    }

    @Test
    void testIfProductDoesNotExist() {
        when(productRepo.existsById("ddd")).thenThrow(new ProductNotFoundException(String.format("Product with id %s is not found!", "ddd")));
        ProductNotFoundException productNotFoundException = assertThrows(ProductNotFoundException.class, () -> productService.checkById("ddd"));
        assertTrue(productNotFoundException.getMessage().contains("not found!"));
    }

    @Test
    void productNotFound() {
        when(productRepo.findById("ddd")).thenThrow(new ProductNotFoundException("Product not found!"));
        ProductNotFoundException ddd = assertThrows(ProductNotFoundException.class, () -> productService.getById("ddd"));
        assertTrue(ddd.getMessage().contains("Product not found!"));
    }

    @Test
    void getByIdWithCorrectProductId() {
        when(productRepo.findById("1")).thenReturn(Optional.of(product));

        Product byId = productService.getById(product.getId());

        assertThat(byId).isNotNull();
    }



    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void checkById() {
//        String id = "cat1";
//
//        willDoNothing().given(categoryRepo).deleteById(id);
//        when(categoryRepo.existsById("cat1")).thenReturn(true);
//
//        productService.delete(id);
//
//        verify(categoryRepo, times(1)).deleteById(id);
    }

    @Test
    void checkIfThereIsEnoughProductInWarehouse() {
    }

    @Test
    void boughtProduct() {
    }
}