package pre_project24.SpringSecurity.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pre_project24.SpringSecurity.model.*;
import pre_project24.SpringSecurity.service.RegistrationService;
import pre_project24.SpringSecurity.service.RoleService;
import pre_project24.SpringSecurity.util.UserValidator;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final RoleService roleService;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthController(UserValidator userValidator,
                          RegistrationService registrationService,
                          RoleService roleService,
                          AuthenticationManager authenticationManager) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.roleService = roleService;
        this.authenticationManager = authenticationManager;

    }

    @GetMapping("/roles")
    public ResponseEntity<?> getRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();
            return ResponseEntity.ok(roles);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка загрузки ролей");
        }
    }

    @PostMapping("/registration.html")
    public String registerUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        userValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return "redirect:/registration";  // Если ошибки при регистрации, перенаправляем обратно на страницу регистрации
        }

        Optional<User> registeredUser = registrationService.registerUser(userDTO);
        if (registeredUser.isPresent()) {
            // Выводим информацию о пользователе в консоль
            User user = registeredUser.get();
//            System.out.println("Пользователь зарегистрирован:");
//            System.out.println("Имя: " + user.getFirstName());
//            System.out.println("Фамилия: " + user.getLastName());
//            System.out.println("Email: " + user.getEmail());
//            System.out.println("Возраст: " + user.getAge());
//            System.out.println("Роли: " + user.getRoles());

            return "redirect:/login";  // Перенаправляем на страницу логина
        } else {
            System.out.println("ERROR");// Если произошла ошибка при регистрации, перенаправляем обратно на страницу регистрации
            return "redirect:/registration";
        }
    }
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
//        try {
//            registrationService.login(loginRequest, request);
//            return ResponseEntity.ok("Login successful");
//        } catch (AuthenticationException e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//        }
//    }
}
