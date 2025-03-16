package pre_project24.SpringSecurity.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pre_project24.SpringSecurity.repositories.RoleRepository;
import pre_project24.SpringSecurity.servises.RoleService;
import pre_project24.SpringSecurity.servises.UserDetailsServiceImpl;

import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final UserDetailsServiceImpl userDetailsService;
    private final RoleService roleService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserDetailsServiceImpl userDetailsService, RoleService roleService, RoleRepository roleRepository) {
        this.userDetailsService = userDetailsService;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String adminPage(Model model) {
        model.addAttribute("users", userDetailsService.getAllUsers());
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin";
    }

    @PostMapping("/updateRoles")
    public String updateUserRoles(@RequestParam Long userId,
                                  @RequestParam(required = false) List<Long> roleIds,
                                  @RequestParam(required = false, defaultValue = "false") boolean removeRoles) {
        if (removeRoles) {
            userDetailsService.removeUserRoles(userId, roleIds);
        } else {
            userDetailsService.updateUserRoles(userId, roleIds);
        }
        return "redirect:/admin";
    }
}
