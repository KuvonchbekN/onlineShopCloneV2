package uz.exadel.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.product.entity.Category;
import uz.exadel.product.payload.CategoryDto;
import uz.exadel.product.repo.CategoryRepo;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource("/application-test.properties")
@ExtendWith(SpringExtension.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    ObjectMapper objectMapper;

    private Category category;

    private CategoryDto categoryDto;

    private static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;


    @BeforeEach
    void setUp() {
        categoryDto = new CategoryDto("Phones2", "Phones section");
        category = new Category("1", "Phones", "This section belongs to the phones", null, Timestamp.valueOf(LocalDateTime.now()));
    }

    @Test
    void addCategory() throws Exception {
        this.mockMvc.perform(post("/api/category")
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object", isA(String.class)));


        List<Category> all = categoryRepo.findAll();
        assertEquals(all.size(), 1);
    }

    @Test
    void getAllCategories() throws Exception {
        categoryRepo.save(category);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/category"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.object", hasSize(1)));
    }

    @Test
    void getCategoryById() throws Exception {
//        categoryRepo.save(category);   => This way is not working, and I don't know why
        jdbc.execute("insert into category (id, created_at, description, name)\n" +
                "values ('1', now(), 'phones section', 'phones');");

        mockMvc.perform(get("/api/category/{categoryId}", "1"))
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.id", is("1")))
                .andExpect(jsonPath("$.object.name", is("phones")))
                .andExpect(jsonPath("$.object.description", is("phones section")));
    }


    @Test
    void updateCategory() throws Exception {
        jdbc.execute("insert into category (id, created_at, description, name)\n" +
                "values ('1', now(), 'phones section', 'phones');");

        mockMvc.perform(put("/api/category/{categoryId}", "1")
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object", is("1")));
    }

    @Test
    void deleteCategory() throws Exception {
        jdbc.execute("insert into category (id, created_at, description, name)\n" +
                "values ('1', now(), 'phones section', 'phones');");

        mockMvc.perform(delete("/api/category/{categoryId}", "1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Deleted Successfully")));


    }

//    @AfterEach
//    void tearDown() {
//        categoryRepo.deleteAll();
//    }
}