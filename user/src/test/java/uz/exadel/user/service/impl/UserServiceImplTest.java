package uz.exadel.user.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.amqp.RabbitMQMessageProducer;
import uz.exadel.user.clients.session.SessionClient;
import uz.exadel.user.clients.session.ShoppingSessionDto;
import uz.exadel.user.dto.UserDto;
import uz.exadel.user.entity.User;
import uz.exadel.user.exception.UserNotFoundException;
import uz.exadel.user.mapper.UserMapperImpl;
import uz.exadel.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Transactional
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapperImpl userMapper;

    @Mock
    private SessionClient sessionClient;

    @Mock
    private RabbitMQMessageProducer rabbitMQMessageProducer;

    //models
    private UserDto userDto;
    private User user;
    LocalDate localDate  = LocalDate.now();
    private List<User> list = new ArrayList<>();

    @BeforeEach
    void setUp() {
        userDto = new UserDto("aaa", "aaa","aaa","aaa","aaa","aaa",localDate, LocalDateTime.now());
        user = new User("1", "aaa", "aaa","aaa","aaa","aaa","aaa",localDate, LocalDateTime.now());
        list.add(user);
    }

    @Test
    void testRegisterUser() {
        lenient().when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);
        lenient().when(userMapper.dtoToUser(any(UserDto.class))).thenReturn(user);
        lenient().doNothing().when(rabbitMQMessageProducer).publish(any(Object.class),any(String.class),any(String.class));
        lenient().when(sessionClient.createSession(any(ShoppingSessionDto.class))).thenReturn(ResponseEntity.ok(null));
        String id = userService.registerUser(userDto);

        assertEquals(id, "1");

    }

    @Test
    void testGetUserList() {
        when(userRepository.findAll()).thenReturn(list);
        List<User> userList = userService.getUserList();

        assertEquals(userList.size(), 1);
    }

    @Test
    void testGetUserById() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        User userById = userService.getUserById("1");

        assertSame(userById, user);

    }

    @Test
    void tryToGetUserButUserNotFound(){
        when(userRepository.findById("2")).thenThrow(new UserNotFoundException("User with this id is not found!"));

        UserNotFoundException userNotFoundException = assertThrows(UserNotFoundException.class, () -> userService.getUserById("2"));

        String message = userNotFoundException.getMessage();

        assertEquals(message, "User with this id is not found!");
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById("1");
        when(userRepository.existsById("1")).thenReturn(true);

        userService.deleteUser("1");
        verify(userRepository,times(1)).deleteById("1");
    }

    @Test
    void testUpdateUser() {
        when(userRepository.existsById("1")).thenReturn(true);
        when(userMapper.dtoToUser(any(UserDto.class))).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        String id = userService.updateUser("1", userDto);
        assertEquals(id, user.getId());
    }
}