package pre_project24.SpringSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.service.RoleService;
import pre_project24.SpringSecurity.service.UserDetailsServiceImpl;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserDetailsServiceImpl userDetailsService;
    private final RoleService roleService;
    private final UserDetailsServiceImpl userService;

    @Autowired
    public AdminController(UserDetailsServiceImpl userDetailsService, RoleService roleService, UserDetailsServiceImpl userService) {
        this.userDetailsService = userDetailsService;
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("users", userDetailsService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin";
    }

    @GetMapping("/user/{id}")
    public String getUserById(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> userOptional = userService.findById(id);

        if (userOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User with ID " + id + " not found!");
            return "redirect:/admin";
        }
        model.addAttribute("user", userOptional.get());
        return "user";
    }

    @PostMapping("admin/updateRoles")
    public String updateUserRoles(@RequestParam Long userId,
                                  @RequestParam(required = false) List<Long> roleIds) {
        userDetailsService.updateUserRoles(userId, roleIds);
        return "redirect:/admin";
    }
}
