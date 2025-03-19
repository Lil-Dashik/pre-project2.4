package pre_project24.SpringSecurity.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pre_project24.SpringSecurity.model.Role;
import pre_project24.SpringSecurity.model.RoleName;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.repository.RoleRepository;
import pre_project24.SpringSecurity.repository.UserRepository;

import java.util.Set;

@Component
public class AdminUserInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminUserInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByEmail("admin@admin.com").isEmpty()) {
            User admin = new User();
            admin.setFirstName("admin_user");
            admin.setLastName("Admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setAge(18);
            admin.setEmail("admin@admin.com");

            Role adminRole = roleRepository.findByRole(RoleName.ROLE_ADMIN).orElseThrow();
            Role userRole = roleRepository.findByRole(RoleName.ROLE_USER).orElseThrow();
            admin.setRoles(Set.of(adminRole, userRole));

            userRepository.save(admin);
            System.out.println("Admin user created successfully!");
        } else {
            System.out.println("Admin user already exists.");
        }
    }
}

