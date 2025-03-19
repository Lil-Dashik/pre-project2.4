package pre_project24.SpringSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pre_project24.SpringSecurity.model.User;


import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminService(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public String getAdminPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        model.addAttribute("page", "admin");
        model.addAttribute("pageTitle", "Admin Panel");
        model.addAttribute("user", userDetails);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        model.addAttribute("editUser", new User());
        return "admin";
    }

    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "addUser";
    }

    public String addUser(User user, List<Long> roleIds, RedirectAttributes redirectAttributes) {
        try {
            userService.addUserWithRoles(user, roleIds);
            redirectAttributes.addFlashAttribute("success", "User added successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error adding user: " + e.getMessage());
        }
        return "redirect:/admin";
    }

    public String getUserById(Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "User with id " + id + " not found");
            return "redirect:/admin";
        }
        model.addAttribute("user", userOptional.get());
        return "user";
    }

    public String updateUser(Long id, List<Long> roleIds) {
        userService.updateUserRoles(id, roleIds);
        return "redirect:/admin";
    }

    public Optional<User> getUser(Long id) {
        return userService.findById(id);
    }

    public String editUserForm(Long id, Model model) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
        model.addAttribute("editUser", user);
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "editAndDeleteUser";
    }

    public String editUser(User editUser, List<Long> roleIds, RedirectAttributes redirectAttributes) {
        if (editUser.getId() == null) {
            redirectAttributes.addFlashAttribute("error", "ID not transmitted");
            return "redirect:/admin";
        }
        User user = userService.findById(editUser.getId()).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "User with id " + editUser.getId() + " not found");
            return "redirect:/admin";
        }
        user.setFirstName(editUser.getFirstName());
        user.setLastName(editUser.getLastName());
        user.setAge(editUser.getAge());
        user.setEmail(editUser.getEmail());
        if (!editUser.getPassword().isEmpty()) {
            user.setPassword(editUser.getPassword());
        }
        user.setRoles(roleService.getRolesByIds(roleIds));
        userService.updateUser(user);
        redirectAttributes.addFlashAttribute("success", "User updated successfully");
        return "redirect:/admin";
    }

    public String deleteUser(Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "User deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting user: " + e.getMessage());
        }
        return "redirect:/admin";
    }
}