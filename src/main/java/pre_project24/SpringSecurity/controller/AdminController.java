package pre_project24.SpringSecurity.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import pre_project24.SpringSecurity.model.RoleDTO;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.model.UserDTO;
import pre_project24.SpringSecurity.repository.RoleRepository;
import pre_project24.SpringSecurity.service.AdminService;
import pre_project24.SpringSecurity.service.UserMapper;

import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;


    @Autowired
    public AdminController(AdminService adminService, RoleRepository roleRepository, UserMapper userMapper) {
        this.adminService = adminService;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> userDTOs = adminService.getAllUsers();
        return ResponseEntity.ok(userDTOs);
    }

    @GetMapping("")
    public ResponseEntity<UserDTO> adminPage(@AuthenticationPrincipal User userDetails) {
        UserDTO userDTO = userMapper.toUserDTO(userDetails);
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            UserDTO userDTO = adminService.getUserById(id);
            return ResponseEntity.ok(userDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addUser")
    public ResponseEntity<UserDTO> addUser(@RequestBody @Valid UserDTO userDTO) {
        UserDTO createdUser = adminService.addUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/users/{id}/roles")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return adminService.updateUser(id, userDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = adminService.deleteUser(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleRepository.findAll()
                .stream()
                .map(role -> new RoleDTO(role.getId(), role.getRole().name()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(roles);
    }
}