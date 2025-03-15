package pre_project24.SpringSecurity.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pre_project24.SpringSecurity.models.Role;
import pre_project24.SpringSecurity.models.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(RoleName roleName);
}
