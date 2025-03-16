package pre_project24.SpringSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pre_project24.SpringSecurity.model.Role;
import pre_project24.SpringSecurity.model.RoleName;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.repository.RoleRepository;
import pre_project24.SpringSecurity.repository.UserRepository;

import java.util.Set;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void registerUser(User user) {
        Role userRole = roleRepository.findByRole(RoleName.ROLE_USER).get();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(userRole));
        userRepository.save(user);
    }

}
