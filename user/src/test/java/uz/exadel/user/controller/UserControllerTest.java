package uz.exadel.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.user.dto.UserDto;
import uz.exadel.user.entity.User;
import uz.exadel.user.repository.UserRepository;
import uz.exadel.user.service.UserService;
import uz.exadel.user.service.impl.UserServiceImpl;

import javax.print.attribute.standard.Media;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource("/application-test.properties")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private ObjectMapper objectMapper;

    private UserDto userDto;
    private User user;
    private
    LocalDate localDate  = LocalDate.now();
    private final List<User> list = new ArrayList<>();

    @BeforeEach
    void setUp() {
        userDto = new UserDto("aaa", "aaa","aaa","aaa","aaa","aaa",localDate.minus(Period.ofDays(10)), LocalDateTime.now());
        user = new User("1", "aaa", "aaa","aaa","aaa","aaa","aaa",localDate.minus(Period.ofDays(10)), LocalDateTime.now());
        list.add(user);
    }


    @Test
    void registerUser() throws Exception {
        mockMvc.perform(post("/api/user").content(objectMapper.writeValueAsString(userDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object",isA(String.class)));
    }

    @Test
    void getUserList() throws Exception {

        userRepository.save(user);

        mockMvc.perform(get("/api/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object", hasSize(1)));
    }

    @Test
    void getUserById() throws Exception {

        User save = userRepository.save(user);
        mockMvc.perform(get("/api/user/{id}", save.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object.id", isA(String.class)))
                .andExpect(jsonPath("$.object.firstName", is(user.getFirstName())))
                .andExpect(jsonPath("$.object.lastName", is(user.getLastName())));
    }

    @Test
    void updateUser() throws Exception {
        User save = userRepository.save(user);
        mockMvc.perform(put("/api/user/{id}",save.getId())
                .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.object", is(save.getId())));
    }

//    @Test
//    void deleteUser() throws Exception {
//        User save = userRepository.save(user);
//        mockMvc.perform(delete("/api/user/{id}", save.getId()))
//                .andExpect(status().isOk());
//    }
}