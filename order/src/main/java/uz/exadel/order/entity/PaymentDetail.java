package uz.exadel.order.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDetail {
    //TODO base abstract classni ham ob cqwm kerak. Id, createdAt fieldlari ucun
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false, name = "total_price")
    private BigDecimal totalPrice;

    @OneToOne(mappedBy = "paymentDetail")
    private OrderDetail orderDetails;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
}