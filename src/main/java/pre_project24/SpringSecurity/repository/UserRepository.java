package pre_project24.SpringSecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pre_project24.SpringSecurity.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
