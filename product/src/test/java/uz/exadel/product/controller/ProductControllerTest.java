package uz.exadel.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.product.entity.Category;
import uz.exadel.product.entity.Product;
import uz.exadel.product.payload.ProductDto;
import uz.exadel.product.repo.CategoryRepo;
import uz.exadel.product.repo.ProductRepo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = "/application-test.properties")
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    private Product product;

    private Product product2;

    private static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    private Category category;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        category = new Category(null, "Phones", "Phones section", null, Timestamp.valueOf(LocalDateTime.now()));
        category = categoryRepo.save(category);
        product = new Product(null, "Apple", "nums", "IphoneX", "The best phones of apple inc.", "11110000", 100, category, BigDecimal.valueOf(100L), 10, Timestamp.valueOf(LocalDateTime.now()));
        product2 = new Product(null, "Samsung", "nums", "SAMSUNG INC.", "The best phones of Samsung company inc.", "111100001", 200, category, BigDecimal.valueOf(200L), 10, Timestamp.valueOf(LocalDateTime.now()));
        productRepo.save(product);
        productRepo.save(product2);
    }

    @Test
    void getAllProductList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.object", hasSize(2)));
    }

    @Test
    void getProductById() throws Exception {
        Product productToSave = new Product(null, "Redmi", "nums", "Redmi INC.", "The best phones of redmi company inc.", "1111000010", 200, category, BigDecimal.valueOf(200L), 10, Timestamp.valueOf(LocalDateTime.now()));
        Product save = productRepo.save(productToSave);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/product/{productId}", save.getId()))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.id", is(save.getId())))
                .andExpect(jsonPath("$.object.name", is(save.getName())))
                .andExpect(jsonPath("$.object.description", is(save.getDescription())));
    }

    @Test
    void addProduct() throws Exception {
        ProductDto productDto = new ProductDto(product.getName(), product.getDescription(), product.getManufacturer(),product.getUnit(),product.getSKU()+"001", product.getPrice(),product.getQuantity(),product.getDiscount(),product.getCategory().getId());
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
                        .content(objectMapper.writeValueAsString(productDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object", isA(String.class)));


        List<Product> all = productRepo.findAll();
        assertEquals(all.size(), 3);
    }

    @Test
    void updateProduct() throws Exception{

        ProductDto productDto = new ProductDto(product.getName(), product.getDescription(), product.getManufacturer(),product.getUnit(),product.getSKU()+"001", product.getPrice(),product.getQuantity(),product.getDiscount(),product.getCategory().getId());


        mockMvc.perform(put("/api/product/{productId}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object", is(product.getId())));
    }

    @Test
    void deleteProduct() throws Exception {
        mockMvc.perform(delete("/api/product/{productId}", product.getId()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        assertEquals(productRepo.findAll().size(), 1);
    }
}