package uz.exadel.session.service;

import org.springframework.stereotype.Service;
import uz.exadel.session.entity.CartItem;
import uz.exadel.session.payload.CartItemDto;

import java.util.List;

@Service
public interface CartService extends BaseService<CartItemDto, CartItem>{
    List<CartItem> getCartItemListBySessionId(String sessionId);
}
