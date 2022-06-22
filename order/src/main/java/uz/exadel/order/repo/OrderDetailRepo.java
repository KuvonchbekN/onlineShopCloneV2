package uz.exadel.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.exadel.order.entity.OrderDetail;

public interface OrderDetailRepo extends JpaRepository<OrderDetail, String> {
}
