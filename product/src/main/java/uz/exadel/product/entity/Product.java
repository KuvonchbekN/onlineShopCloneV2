package uz.exadel.product.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    private String manufacturer;

    private String unit;

    private String name;

    private String description;

    private String SKU; //each type of product has different sku number

    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
//    @JsonBackReference
    @JsonIgnore
    private Category category;

    private BigDecimal price;

    private int discount;

    @JsonIgnore
    @Column(updatable = false)
    @CreationTimestamp
    private Timestamp createdAt;

}
