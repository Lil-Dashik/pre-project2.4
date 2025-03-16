package pre_project24.SpringSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.security.UsersDetailsImp;
import pre_project24.SpringSecurity.service.UserDetailsServiceImpl;

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

    @GetMapping("/users")
    public String getUsersList(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin";
    }


}
