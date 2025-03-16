package pre_project24.SpringSecurity.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pre_project24.SpringSecurity.models.Role;
import pre_project24.SpringSecurity.models.RoleName;
import pre_project24.SpringSecurity.models.User;
import pre_project24.SpringSecurity.repositories.RoleRepository;
import pre_project24.SpringSecurity.repositories.UserRepository;

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
        if (userRepository.findByUsername("admin_user").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin_user");
            admin.setPassword(passwordEncoder.encode("admin"));

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

