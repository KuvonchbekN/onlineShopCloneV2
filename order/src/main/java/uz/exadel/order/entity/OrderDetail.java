package uz.exadel.order.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Set;


@Entity
@Table(name = "order_detail")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderDetail {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(nullable = false, name = "user_id")
    private String userId;

    @OneToOne
    @JoinColumn(name = "payment_detail_id")
    private PaymentDetail paymentDetail;

    @OneToMany(mappedBy = "orderDetail")
    private Set<OrderItem> orderItems;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
}
