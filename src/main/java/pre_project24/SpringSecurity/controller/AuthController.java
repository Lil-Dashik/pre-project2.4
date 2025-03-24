package pre_project24.SpringSecurity.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
//    private final AuthenticationManager authenticationManager;


    @Autowired
    public AuthController(UserValidator userValidator,
                          RegistrationService registrationService,
                          RoleService roleService,
                          AuthenticationManager authenticationManager) {
        this.userValidator = userValidator;
        this.registrationService = registrationService;
        this.roleService = roleService;
//        this.authenticationManager = authenticationManager;

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
            return "redirect:/registration";
        }
        Optional<User> registeredUser = registrationService.registerUser(userDTO);
        if (registeredUser.isPresent()) {

//            User user = registeredUser.get();
            return "redirect:/login";
        } else {
            System.out.println("ERROR");
            return "redirect:/registration";
        }
    }
}

