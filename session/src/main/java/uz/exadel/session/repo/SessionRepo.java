package uz.exadel.session.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.exadel.session.entity.ShoppingSession;

import java.util.Optional;

public interface SessionRepo extends JpaRepository<ShoppingSession, String> {
    Optional<ShoppingSession> findByUserId(String userId);

    boolean existsByUserId(String userId);

    void deleteByUserId(String userId);
}
