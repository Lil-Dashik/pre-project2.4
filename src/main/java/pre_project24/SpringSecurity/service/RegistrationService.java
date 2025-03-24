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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        // Выводим информацию о данных, полученных в UserDTO
//        System.out.println("UserDTO to be registered:");
//        System.out.println("First Name: " + userDTO.getFirstName());
//        System.out.println("Last Name: " + userDTO.getLastName());
//        System.out.println("Email: " + userDTO.getEmail());
//        System.out.println("Age: " + userDTO.getAge());
//        System.out.println("Roles: " + userDTO.getRoleIds());  // Список ID ролей из DTO (если роли не передаются, это может быть null)

        // Преобразуем UserDTO в User
        User user = userMapper.fromUserDTO(userDTO);

        // Ищем роль для пользователя
        Optional<Role> userRole = roleRepository.findByRole(RoleName.ROLE_USER);
        if (userRole.isEmpty()) {
//            // Логируем, если роль не найдена
//            System.out.println("ROLE_USER not found in the database!");
            return Optional.empty();  // Если не нашли роль, возвращаем пустой результат
        }

        // Кодируем пароль пользователя
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Присваиваем пользователю роль ROLE_USER
        user.setRoles(Set.of(userRole.get()));  // Устанавливаем роль

        // Выводим информацию о пользователе перед сохранением
//        System.out.println("User to be registered:");
//        System.out.println("First Name: " + user.getFirstName());
//        System.out.println("Last Name: " + user.getLastName());
//        System.out.println("Email: " + user.getEmail());
//        System.out.println("Age: " + user.getAge());
//        System.out.println("Roles: " + user.getRoles());

        // Сохраняем пользователя в базу данных
        User savedUser = userRepository.save(user);

//        // Выводим информацию о сохраненном пользователе
//        System.out.println("User saved with ID: " + savedUser.getId());

        return Optional.of(savedUser);  // Возвращаем сохраненного пользователя
    }
    public void login(LoginRequest loginRequest, HttpServletRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        request.getSession(true);
    }
}

