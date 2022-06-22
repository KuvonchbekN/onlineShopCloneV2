package uz.exadel.order.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.exadel.order.entity.PaymentDetail;

public interface PaymentDetailRepo extends JpaRepository<PaymentDetail, String> {
}
