package uz.exadel.session.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.exadel.session.entity.CartItem;
import uz.exadel.session.entity.ShoppingSession;
import uz.exadel.session.exception.CartNotFoundException;
import uz.exadel.session.payload.CartItemDto;
import uz.exadel.session.repo.CartItemRepo;
import uz.exadel.session.service.CartService;
import uz.exadel.session.service.SessionService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepo cartItemRepo;
    private final SessionService sessionService;

    @Override
    public String create(CartItemDto cartItemDto) {
        ShoppingSession session = sessionService.getById(cartItemDto.getSessionId());
        CartItem cartItem = CartItem.builder()
                .quantity(cartItemDto.getQuantity())
                .shoppingSession(session)
                .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                .productId(cartItemDto.getProductId())
                .build();
        CartItem save = cartItemRepo.save(cartItem);

        return save.getId();
    }

    @Override
    public List<CartItem> getList() {
        return cartItemRepo.findAll();
    }


    @Override
    public CartItem getById(String id) {
        return cartItemRepo.findById(id)
                .orElseThrow(()->new CartNotFoundException("Cart with this id is not found"));
    }


    @Override
    public void update(CartItemDto cartItemDto, String id) {
        sessionService.existsById(cartItemDto.getSessionId());
        Optional<CartItem> byId = cartItemRepo.findById(id);
        if (byId.isEmpty()){
            throw new CartNotFoundException("Cart with this id is not found!");
        }

        ShoppingSession session = sessionService.getById(cartItemDto.getSessionId());

        CartItem cartItem = byId.get();
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItem.setProductId(cartItemDto.getProductId());
        cartItem.setShoppingSession(session);
        cartItemRepo.save(cartItem);
    }

    @Override
    public void delete(String id) {
        cartItemRepo.deleteById(id);
    }

    @Override
    public List<CartItem> getCartItemListBySessionId(String sessionId) {
        sessionService.existsById(sessionId); //throws exception if not found!
        return cartItemRepo.getCartItemsByShoppingSession_Id(sessionId);
    }

    public void checksById(String id){
        boolean exists = cartItemRepo.existsById(id);
        if (!exists){
            throw new CartNotFoundException("Cart with this id is not found!");
        }
    }
}
