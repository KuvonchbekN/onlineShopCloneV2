package uz.exadel.session.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.session.entity.ShoppingSession;
import uz.exadel.session.payload.ShoppingSessionDto;
import uz.exadel.session.repo.SessionRepo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(value = "/application-test.properties")
@Transactional
class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepo sessionRepo;

    private static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @Autowired
    private ObjectMapper objectMapper;

    private ShoppingSession shoppingSession;
    private ShoppingSessionDto shoppingSessionDto;
    private final List<ShoppingSessionDto> shoppingSessionDtoList = new ArrayList<>();
    private final List<ShoppingSession> shoppingSessionList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        shoppingSession = new ShoppingSession("1", "1", BigDecimal.valueOf(10), null, Timestamp.valueOf(LocalDateTime.now()));
        shoppingSessionDto = new ShoppingSessionDto("1", BigDecimal.valueOf(10));

        shoppingSessionList.add(shoppingSession);
        shoppingSessionDtoList.add(shoppingSessionDto);
    }


    @Test
    void createSession() throws Exception {
        mockMvc.perform(post("/api/session")
                        .content(objectMapper.writeValueAsString(shoppingSessionDto)).contentType(APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    void getSessionList() throws Exception {
        sessionRepo.save(shoppingSession);
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object", hasSize(1)));
    }

    @Test
    void getSessionById() throws Exception {
        ShoppingSession save = sessionRepo.save(shoppingSession);
        mockMvc.perform(get("/api/session/{id}", save.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.userId", is("1")));
    }

    @Test
    void getSessionByUserId() throws Exception {
        ShoppingSession save = sessionRepo.save(shoppingSession);
        mockMvc.perform(get("/api/session/userId/{userId}", save.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.id", is(save.getId())));
    }

    @Test
    void updateSession() throws Exception {
        ShoppingSession save = sessionRepo.save(shoppingSession);
        mockMvc.perform(put("/api/session/{id}", save.getId()).contentType(APPLICATION_JSON_UTF8).content(objectMapper.writeValueAsString(shoppingSessionDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteSession() throws Exception {
        ShoppingSession save = sessionRepo.save(shoppingSession);
        mockMvc.perform(delete("/api/session/{id}", save.getId()))
                .andExpect(status().isOk());

    }

    @Test
    void deleteUserSessionWhenUserDeleted() throws Exception {
        ShoppingSession save = sessionRepo.save(shoppingSession);
        mockMvc.perform(delete("/api/session/userSession/{userId}", save.getUserId()))
                .andExpect(status().isOk());
    }
}