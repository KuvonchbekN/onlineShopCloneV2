package uz.exadel.order.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_item")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderItem {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_detail_id", nullable = false)
    private OrderDetail orderDetail;

    @Column(name = "product_id", nullable = false,unique = true)
    private String productId;

    @Column(name = "product_quantity")
    private int productQuantity; //this is the quantity of each bought product

    private BigDecimal price;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt = Timestamp.valueOf(LocalDateTime.now());
}
