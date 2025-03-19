package pre_project24.SpringSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.service.RegistrationService;
import pre_project24.SpringSecurity.service.RoleService;
import pre_project24.SpringSecurity.util.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
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

    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String loginPage(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "auth/registration";
        }
        registrationService.registerUser(user);
        return "/user";
    }
}
