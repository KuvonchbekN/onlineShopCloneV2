package uz.exadel.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.exadel.product.entity.Product;

public interface ProductRepo extends JpaRepository<Product, String> {
    boolean existsBySKU(String SKU);

    @Query("select p.quantity from product p where p.id = ?1")
    int findProductInformationByProductId(String productId);

    @Query("select p.name from product p where p.id = ?1")
    String findNameById(String id);

}
