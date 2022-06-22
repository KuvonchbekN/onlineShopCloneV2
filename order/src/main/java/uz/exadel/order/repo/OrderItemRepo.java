package uz.exadel.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.exadel.order.entity.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem, String > {
}
