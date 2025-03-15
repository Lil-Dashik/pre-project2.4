package pre_project24.SpringSecurity.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pre_project24.SpringSecurity.models.User;
import pre_project24.SpringSecurity.security.UsersDetailsImp;
import pre_project24.SpringSecurity.servises.UserDetailsServiceImpl;

import java.util.List;

@Controller
@RequestMapping("")
public class UserController {
    private final UserDetailsServiceImpl userService;

    @Autowired
    public UserController(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping(value = "/")
    public String getHomePage() {
        return "index";
    }

    @GetMapping(value = "/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping(value = "/user")
    public String getUserPage(Model model, @AuthenticationPrincipal UsersDetailsImp userDetails) {
        if (userDetails != null) {
            model.addAttribute("user", userDetails.getUser());
        }
        return "user";
    }

    @GetMapping(value = "/admin")
    public String getAdminPage(Model model, @AuthenticationPrincipal UsersDetailsImp userDetails) {
        if (userDetails != null) {
            model.addAttribute("user", userDetails.getUser());
        }
        return "admin";
    }

    @GetMapping("/users")
    public String getUsersList(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
}
