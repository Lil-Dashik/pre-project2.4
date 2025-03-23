package pre_project24.SpringSecurity.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
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


    @Autowired
    public AuthController(UserValidator userValidator, RegistrationService registrationService, RoleService roleService) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.roleService = roleService;

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
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO userDTO, BindingResult bindingResult) {
        userValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        Optional<User> registeredUser = registrationService.registerUser(userDTO);

        return registeredUser
                .map(u -> ResponseEntity.ok("User registered successfully"))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Default role not found"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        try {
            registrationService.login(loginRequest, request);
            return ResponseEntity.ok("Login successful");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }
}
