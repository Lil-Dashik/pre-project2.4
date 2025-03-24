package pre_project24.SpringSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pre_project24.SpringSecurity.model.*;
import pre_project24.SpringSecurity.repository.RoleRepository;
import pre_project24.SpringSecurity.repository.UserRepository;

import java.util.Optional;
import java.util.Set;


@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Autowired
    public RegistrationService(UserRepository userRepository, RoleRepository roleRepository,
                               PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public Optional<User> registerUser(UserDTO userDTO) {
        User user = userMapper.fromUserDTO(userDTO);

        Optional<Role> userRole = roleRepository.findByRole(RoleName.ROLE_USER);
        if (userRole.isEmpty()) {
            return Optional.empty();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(userRole.get()));
        User savedUser = userRepository.save(user);
        return Optional.of(savedUser);
    }
}

