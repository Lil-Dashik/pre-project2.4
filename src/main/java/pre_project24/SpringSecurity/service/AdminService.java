package pre_project24.SpringSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pre_project24.SpringSecurity.model.Role;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.repository.UserRepository;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AdminService {
    private final UserService userService;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public AdminService(UserService userService, RoleService roleService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

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

    public String updateUserPassword(User editUser) {
        User user = userRepository.findById(editUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(editUser.getFirstName());
        user.setLastName(editUser.getLastName());
        user.setEmail(editUser.getEmail());

        if (editUser.getPassword() != null && !editUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(editUser.getPassword()));
        }

        userRepository.save(user);
        return "redirect:/admin";
    }

    public Optional<User> getUser(Long id) {
        return userService.findById(id);
    }
    public String updateUser(Long userId, List<Long> roleIds) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> updatedRoles = new HashSet<>(roleService.getRolesByIds(roleIds));
            user.setRoles(updatedRoles);
        }

        userService.updateUser(user);
        return "redirect:/admin";
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
            redirectAttributes.addFlashAttribute("error", "ID не передан!");
            return "redirect:/admin";
        }

        User user = userRepository.findById(editUser.getId()).orElse(null);
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Пользователь не найден!");
            return "redirect:/admin";
        }

        user.setFirstName(editUser.getFirstName());
        user.setLastName(editUser.getLastName());
        user.setEmail(editUser.getEmail());

        if (editUser.getPassword() != null && !editUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(editUser.getPassword()));
        }

        user.setRoles(roleService.getRolesByIds(roleIds));
        userRepository.save(user);

        redirectAttributes.addFlashAttribute("success", "Пользователь успешно обновлен!");
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