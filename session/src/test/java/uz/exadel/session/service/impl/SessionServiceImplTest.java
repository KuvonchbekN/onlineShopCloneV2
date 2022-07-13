package uz.exadel.session.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.session.entity.ShoppingSession;
import uz.exadel.session.exception.SessionNotFoundException;
import uz.exadel.session.payload.ShoppingSessionDto;
import uz.exadel.session.repo.SessionRepo;

import java.math.BigDecimal;
import java.sql.Timestamp;
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
class SessionServiceImplTest {

    @Mock
    private SessionRepo sessionRepo;

    @InjectMocks
    private SessionServiceImpl sessionService;


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
    void create() {
        when(sessionRepo.save(any(ShoppingSession.class))).thenReturn(shoppingSession);
        String id = sessionService.create(shoppingSessionDto);

        assertEquals("1", id);
    }

    @Test
    void getShoppingSessionByUserId() {
        when(sessionRepo.findByUserId("1")).thenReturn(Optional.of(shoppingSession));

        ShoppingSession shoppingSessionByUserId = sessionService.getShoppingSessionByUserId("1");

        assertSame(shoppingSessionByUserId, shoppingSession);
    }

    @Test
    void getShoppingSessionByUserId_throwException() {
        when(sessionRepo.findByUserId("1")).thenReturn(Optional.empty());

        assertThrows(SessionNotFoundException.class, ()->sessionService.getShoppingSessionByUserId("1"));
    }


    @Test
    void getList() {
        when(sessionRepo.findAll()).thenReturn(shoppingSessionList);

        List<ShoppingSession> list = sessionService.getList();
        assertEquals(1, list.size());
    }

    @Test
    void getById() {
        when(sessionRepo.findById("1")).thenReturn(Optional.ofNullable(shoppingSession));
        ShoppingSession byId = sessionService.getById("1");
        assertSame(byId, shoppingSession);
    }

    @Test
    void update() {
        lenient().when(sessionRepo.save(any(ShoppingSession.class))).thenReturn(shoppingSession);
        lenient().when(sessionRepo.findById("1")).thenReturn(Optional.empty());

        assertThrows(SessionNotFoundException.class, ()->sessionService.update(shoppingSessionDto, "1"));
    }

    @Test
    void delete() {
        when(sessionRepo.existsById("1")).thenReturn(true);
        doNothing().when(sessionRepo).deleteById("1");
        sessionService.delete("1");
        verify(sessionRepo, times(1)).deleteById("1");
    }

    @Test
    void existsById() {
        when(sessionRepo.existsById("1")).thenReturn(false);
        assertThrows(SessionNotFoundException.class, ()->sessionService.existsById("1"));
    }

    @Test
    void deleteSessionWhenUserDeleted_throwsException() {
        when(sessionRepo.existsByUserId("1")).thenReturn(false);

        assertThrows(SessionNotFoundException.class, ()-> sessionService.deleteSessionWhenUserDeleted("1"));
    }

    @Test
    void deleteSessionWhenUserIsDeleted_successfully(){
        lenient().when(sessionRepo.existsByUserId("1")).thenReturn(true);
        lenient().doNothing().when(sessionRepo).deleteByUserId("1");

        sessionService.deleteSessionWhenUserDeleted("1");

        verify(sessionRepo, times(1)).deleteByUserId("1");
    }


}