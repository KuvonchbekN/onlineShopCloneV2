package uz.exadel.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.math.stat.descriptive.summary.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import uz.exadel.order.dto.OrderCartDto;
import uz.exadel.order.dto.OrderItemDto;
import uz.exadel.order.productClient.ProductClient;
import uz.exadel.order.service.impl.OrderServiceImpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource("/application-test.properties")
@ExtendWith(SpringExtension.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private ObjectMapper objectMapper;

    private OrderCartDto orderCartDto;

    private Product product;

    @Mock
    private ProductClient productClient;



    @BeforeEach
    void setUp() {
        OrderItemDto orderItemDto = new OrderItemDto("1", 10, BigDecimal.valueOf(10));
        List<OrderItemDto> list = new ArrayList<>();
        list.add(orderItemDto);

        orderCartDto = new OrderCartDto(list, "1");
    }

    @Test
    void buyByCart() throws Exception {
        mockMvc.perform(post("/api/order")
                .content(objectMapper.writeValueAsString(orderCartDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object", isA(String.class)));
    }
}