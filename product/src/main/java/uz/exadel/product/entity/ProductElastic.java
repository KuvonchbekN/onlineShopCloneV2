package uz.exadel.product.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Document(indexName = "name")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductElastic {

    @Id
    private String id;

    @Field(name = "manufacturer", type = FieldType.Text)
    private String manufacturer;

    @Field(name = "unit", type = FieldType.Text)
    private String unit;

    @Field(name = "name", type = FieldType.Text)
    private String name;

    @Field(name = "description", type = FieldType.Text)
    private String description;

    @Field(name = "sku", type = FieldType.Text)
    private String SKU; //each type of product has different sku number

    @Field(name = "quantity", type = FieldType.Double)
    private int quantity;

    @Field(name = "price", type = FieldType.Double)
    private BigDecimal price;

    @Field(name = "discount", type = FieldType.Integer)
    private int discount;

    @Field(name = "creationTime", type = FieldType.Date)
    private Timestamp createdAt;
}
