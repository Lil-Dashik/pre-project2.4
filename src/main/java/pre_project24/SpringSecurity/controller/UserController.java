package pre_project24.SpringSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.service.UserService;


@Controller
@RequestMapping("")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
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
    public String getUserPage(Model model, @AuthenticationPrincipal User userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        userService.prepareUserPage(model, userDetails.getEmail());
        return "user";
    }

    @GetMapping("/users")
    public String getUsersList(Model model) {
        userService.usersListPage(model);
        return "admin";
    }


}
