package uz.exadel.session.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.session.entity.CartItem;
import uz.exadel.session.entity.ShoppingSession;
import uz.exadel.session.exception.CartNotFoundException;
import uz.exadel.session.payload.CartItemDto;
import uz.exadel.session.repo.CartItemRepo;
import uz.exadel.session.repo.SessionRepo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Transactional
@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private SessionServiceImpl sessionService;

    @Mock
    private CartItemRepo cartItemRepo;
    @Mock
    private SessionRepo sessionRepo;

    private CartItem cartItem;

    private CartItemDto cartItemDto;

    private ShoppingSession shoppingSession;
    private List<CartItem> cartItemList = new ArrayList<>();


    @BeforeEach
    void setUp() {
        shoppingSession = new ShoppingSession("1", "1", BigDecimal.valueOf(10), null, Timestamp.valueOf(LocalDateTime.now()));
        cartItem = new CartItem("1", 10, "1", shoppingSession,Timestamp.valueOf(LocalDateTime.now()));
        cartItemDto = new CartItemDto(8, "1", "1");
        cartItemList.add(cartItem);
    }

    @Test
    void create() {
        Mockito.when(cartItemRepo.save(any(CartItem.class))).thenReturn(cartItem);
        Mockito.when(sessionService.getById("1")).thenReturn(shoppingSession);

        String id = cartService.create(cartItemDto);
        assertEquals(id, "1");
    }

    @Test
    void getList() {
        Mockito.when(cartItemRepo.findAll()).thenReturn(cartItemList);
        List<CartItem> list = cartService.getList();

        assertEquals(1, list.size());
    }

    @Test
    void getById() {
        Mockito.when(cartItemRepo.findById("1")).thenReturn(Optional.of(cartItem));

        CartItem byId = cartService.getById("1");
        assertEquals(byId, cartItem);
    }

    @Test
    void getById_notFound() {
        Mockito.when(cartItemRepo.findById("1")).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, ()->cartService.getById("1"));
    }

    @Test
    void update() {
//        when(sessionRepo.existsById("1")).thenReturn(true);
        when(cartItemRepo.findById("1")).thenReturn(Optional.of(cartItem));

        cartService.update(cartItemDto, "1");
        verify(cartItemRepo, times(1)).save(any(CartItem.class));

    }

    @Test
    void delete() {
        doNothing().when(cartItemRepo).deleteById("1");

        cartService.delete("1");
        verify(cartItemRepo, times(1)).deleteById("1");
    }

    @Test
    void getCartItemListBySessionId() {
        lenient().when(sessionRepo.existsById("1")).thenReturn(true);
        lenient().when(cartItemRepo.getCartItemsByShoppingSession_Id("1")).thenReturn(cartItemList);

        List<CartItem> cartItemListBySessionId = cartService.getCartItemListBySessionId("1");
        assertEquals(1, cartItemListBySessionId.size());
    }

    @Test
    void checksById_cartNotFound() {
        Mockito.when(cartItemRepo.existsById("1")).thenReturn(false);

        assertThrows(CartNotFoundException.class, ()-> cartService.checksById("1"));
    }

    @Test
    void findById_notFound(){
        Mockito.when(cartItemRepo.findById("1")).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, ()-> cartService.update(cartItemDto, "1"));
    }

}