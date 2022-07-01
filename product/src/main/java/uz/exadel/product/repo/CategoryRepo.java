package uz.exadel.product.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.exadel.product.entity.Category;

public interface CategoryRepo extends JpaRepository<Category, String> {
    boolean existsByName(String name);
}
