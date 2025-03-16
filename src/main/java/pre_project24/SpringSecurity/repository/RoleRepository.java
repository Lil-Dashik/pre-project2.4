package pre_project24.SpringSecurity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pre_project24.SpringSecurity.model.Role;
import pre_project24.SpringSecurity.model.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(RoleName roleName);
}
