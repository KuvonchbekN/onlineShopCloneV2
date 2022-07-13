package uz.exadel.product.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.product.clientDto.OrderItemDto;
import uz.exadel.product.entity.Category;
import uz.exadel.product.entity.Product;
import uz.exadel.product.exception.ProductNotFoundException;
import uz.exadel.product.exception.UnsufficientProductException;
import uz.exadel.product.mappers.CustomMapper;
import uz.exadel.product.mappers.ProductMapperImpl;
import uz.exadel.product.payload.ProductDto;
import uz.exadel.product.repo.CategoryRepo;
import uz.exadel.product.repo.ProductRepo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//@SpringBootTest => this is for integration testing
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Transactional
@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private CategoryRepo categoryRepo;

    @Mock
    private CustomMapper<Product, ProductDto> productMapper = new ProductMapperImpl(categoryRepo);

    @Mock
    private CategoryServiceImpl categoryService ;

    @Mock
    private ProductRepo productRepo;
    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductDto productDto;
    private Category category;
    private OrderItemDto orderItemDto;
    private OrderItemDto orderItemDto2;
    private OrderItemDto orderItemDto3;
    private OrderItemDto orderItemDto4;

    @BeforeEach
    void setUp() {
        category = new Category("1", "aaa", "aaa", null, Timestamp.valueOf(LocalDateTime.now()));
        product = new Product("1", "aaa", "aaa", "aaa", "aaa", "aaa", 10, category, BigDecimal.valueOf(10), 10, Timestamp.valueOf(LocalDateTime.now()));
        productDto = new ProductDto(product.getName(), product.getDescription(),product.getManufacturer(),product.getUnit(),product.getSKU(),product.getPrice(),product.getQuantity(),product.getDiscount(),category.getId());
        orderItemDto = new OrderItemDto(product.getId(), 11, BigDecimal.valueOf(500));
        orderItemDto4 = new OrderItemDto(product.getId(), 5, BigDecimal.valueOf(500));
        orderItemDto3 = new OrderItemDto(product.getId(), 3, BigDecimal.valueOf(500));
        orderItemDto2 = new OrderItemDto(product.getId(), 10, BigDecimal.valueOf(600));
    }

    @DisplayName(value = "Adding New Product")
    @Test
     void create() {
        lenient().when(productRepo.save(any(Product.class))).thenReturn(product);
        lenient().when(productMapper.dtoToObject(any(ProductDto.class))).thenReturn(product);
        lenient().when(productMapper.objectToDto(any(Product.class))).thenReturn(productDto);

        ProductDto productDto = productMapper.objectToDto(product);

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
        ProductNotFoundException productNotFoundException = assertThrows(ProductNotFoundException.class,
                () -> productService.checkById(null));
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
        Product product1 = new Product("1", "bbb", "bbb", "bbb", "bbb", "bbb", 20,category , BigDecimal.valueOf(20), 5, Timestamp.valueOf(LocalDateTime.now()));
        lenient().when(productRepo.save(any(Product.class))).thenReturn(product1);
        lenient().when(productMapper.dtoToObject(any(ProductDto.class))).thenReturn(product);
        lenient().when(productMapper.objectToDto(any(Product.class))).thenReturn(productDto);
        lenient().when(productRepo.existsById("1")).thenReturn(true);

        ProductDto productDto1 = new ProductDto("bbb","bbb","bbb","bbb","bbb",BigDecimal.valueOf(20),20,5,product.getCategory().getId());

        String update = productService.update(productDto1, product.getId());
        assertEquals(update, "1");
    }

    @Test
    void delete() {
        doNothing().when(productRepo).deleteById("1");
        when(productRepo.existsById("1")).thenReturn(true);
        productService.delete("1");
        verify(productRepo, times(1)).deleteById("1");
    }


    @Test
    void checkById_throwException() {
        when(productRepo.existsById("someId")).thenReturn(false);
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> productService.checkById("someId"));
        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    void checkIfThereIsEnoughProductInWarehouse() {
        List<OrderItemDto> list = new ArrayList<>(List.of(orderItemDto, orderItemDto2));
        lenient().when(productRepo.findProductInformationByProductId(product.getId())).thenReturn(product.getQuantity());
        lenient().when(productRepo.findNameById(product.getId())).thenReturn(product.getName());
        lenient().when(productRepo.findById(product.getId())).thenReturn(Optional.of(product));

        UnsufficientProductException unsufficientProductException = assertThrows(UnsufficientProductException.class,
                () -> productService.checkIfThereIsEnoughProductInWarehouse(list));
        assertTrue(unsufficientProductException.getMessage().contains("not requested amount"));
    }

    @Test
    void boughtProduct_successfully() {
        List<OrderItemDto> list = new ArrayList<>(List.of(orderItemDto3, orderItemDto4));
        lenient().when(productRepo.findById("1")).thenReturn(Optional.of(product));

        productService.boughtProduct(list);

        assertEquals(product.getQuantity(), 2);
    }

    @Test
    void boughtProduct_couldNotFindProduct() {
        List<OrderItemDto> list = new ArrayList<>(List.of(orderItemDto3, orderItemDto4));
        lenient().when(productRepo.findById("1")).thenReturn(Optional.empty());

        ProductNotFoundException productNotFoundException = assertThrows(ProductNotFoundException.class, () -> productService.boughtProduct(list));

        assertTrue(productNotFoundException.getMessage().contains("not found"));
    }
}