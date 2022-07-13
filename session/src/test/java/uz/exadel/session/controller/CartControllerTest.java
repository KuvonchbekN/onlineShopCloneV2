package uz.exadel.session.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.session.entity.CartItem;
import uz.exadel.session.entity.ShoppingSession;
import uz.exadel.session.payload.CartItemDto;
import uz.exadel.session.repo.CartItemRepo;
import uz.exadel.session.repo.SessionRepo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Transactional
class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepo sessionRepo;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CartItemRepo cartItemRepo;


    private CartItem cartItem;

    private CartItemDto cartItemDto;

    private ShoppingSession shoppingSession;
    private final List<CartItem> cartItemList = new ArrayList<>();
    private String savedSessionId;


    @BeforeEach
    void setUp() {
        shoppingSession = new ShoppingSession("1", "1", BigDecimal.valueOf(10), null, Timestamp.valueOf(LocalDateTime.now()));
        ShoppingSession save = sessionRepo.save(shoppingSession);
        cartItem = new CartItem("1", 10, "1", save, Timestamp.valueOf(LocalDateTime.now()));
        cartItemDto = new CartItemDto(8, save.getId(), "1");
        cartItemList.add(cartItem);
        savedSessionId = save.getId();
    }

    @Test
    void createCart() throws Exception {
        mockMvc.perform(post("/api/cart")
                        .content(objectMapper.writeValueAsString(cartItemDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getCartList() throws Exception {
        cartItemRepo.save(cartItem);
        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object", hasSize(1)));
    }

    @Test
    void getSessionCartsList() throws Exception {
        cartItemRepo.save(cartItem);
        mockMvc.perform(get("/api/cart//sessionCarts/{sessionId}", savedSessionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object", hasSize(1)));
    }

    @Test
    void getCartById() throws Exception {
        CartItem save = cartItemRepo.save(cartItem);
        mockMvc.perform(get("/api/cart/{id}", save.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.id", is(save.getId())));
    }

    @Test
    void updateCart() throws Exception {
        CartItem save = cartItemRepo.save(cartItem);
        mockMvc.perform(put("/api/cart/{id}", save.getId())
                        .content(objectMapper.writeValueAsString(cartItemDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCart() throws Exception {
        CartItem save = cartItemRepo.save(cartItem);

        mockMvc.perform(delete("/api/cart/{id}", save.getId()))
                .andExpect(status().isOk());
    }
}