package pre_project24.SpringSecurity.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pre_project24.SpringSecurity.model.Role;
import pre_project24.SpringSecurity.model.RoleName;
import pre_project24.SpringSecurity.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            Role userRole = new Role(RoleName.ROLE_USER, new HashSet<>());
            Role adminRole = new Role(RoleName.ROLE_ADMIN, new HashSet<>());
            roleRepository.saveAll(Set.of(userRole, adminRole));
        }
    }
}
