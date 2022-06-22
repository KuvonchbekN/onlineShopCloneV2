package uz.exadel.session.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.exadel.session.entity.CartItem;

import java.util.List;

public interface CartItemRepo extends JpaRepository<CartItem, String> {
    List<CartItem> getCartItemsByShoppingSession_Id(String shoppingSession_id);
}
