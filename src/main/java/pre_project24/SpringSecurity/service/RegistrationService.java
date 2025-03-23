package pre_project24.SpringSecurity.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Autowired
    public RegistrationService(UserRepository userRepository, RoleRepository roleRepository,
                               PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                               UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
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
        return Optional.of(userRepository.save(user));
    }

    public void login(LoginRequest loginRequest, HttpServletRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        request.getSession(true);
    }
}

