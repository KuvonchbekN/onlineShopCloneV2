package uz.exadel.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.exadel.user.entity.User;

public interface UserRepository extends JpaRepository<User, String > {
    boolean existsByEmail(String email);
}
