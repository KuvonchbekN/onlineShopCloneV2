package uz.exadel.session.service;

import org.springframework.stereotype.Service;
import uz.exadel.session.entity.ShoppingSession;
import uz.exadel.session.payload.ShoppingSessionDto;

@Service
public interface SessionService extends BaseService<ShoppingSessionDto, ShoppingSession>{
    ShoppingSession getShoppingSessionByUserId(String userId);

    void existsById(String id);

    void deleteSessionWhenUserDeleted(String userId);
}
