package pre_project24.SpringSecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pre_project24.SpringSecurity.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
