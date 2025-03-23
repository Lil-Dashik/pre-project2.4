package pre_project24.SpringSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pre_project24.SpringSecurity.model.Role;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.repository.RoleRepository;
import pre_project24.SpringSecurity.service.AdminService;
import pre_project24.SpringSecurity.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(AdminService adminService, UserService userService, RoleRepository roleRepository) {
        this.adminService = adminService;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("")
    public ResponseEntity<User> adminPage(@AuthenticationPrincipal User userDetails) {
        return ResponseEntity.ok(userDetails);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = adminService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/addUser")
    public ResponseEntity<User> addUser(@RequestBody Map<String, Object> data) {
        User user = adminService.addUserFromMap(data);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/users/{id}/roles")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> data) {
        return adminService.updateUserFromMap(id, data)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = adminService.deleteUser(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/roles")
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}