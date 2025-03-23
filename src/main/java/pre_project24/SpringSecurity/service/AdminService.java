package pre_project24.SpringSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pre_project24.SpringSecurity.model.Role;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.repository.UserRepository;


import java.util.*;

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

    public Optional<User> getUserById(Long id) {
        return userService.findById(id);
    }

    public User addUser(User user, List<Long> roleIds) {
        Set<Role> roles = new HashSet<>(roleService.getRolesByIds(roleIds));
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public Optional<User> updateUser(Long id, User userDetails, List<Long> roleIds) {
        return userRepository.findById(id).map(user -> {
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setAge(userDetails.getAge());
            user.setEmail(userDetails.getEmail());

            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }

            if (roleIds != null && !roleIds.isEmpty()) {
                Set<Role> updatedRoles = new HashSet<>(roleService.getRolesByIds(roleIds));
                user.setRoles(updatedRoles);
            }

            return userRepository.save(user);
        });
    }

    public User addUserFromMap(Map<String, Object> data) {
        User user = buildUserFromMap(data);
        List<Long> roleIds = extractRoleIds(data);
        return addUser(user, roleIds);
    }

    public Optional<User> updateUserFromMap(Long id, Map<String, Object> data) {
        User userDetails = buildUserFromMap(data);
        List<Long> roleIds = extractRoleIds(data);
        return updateUser(id, userDetails, roleIds);
    }

    private User buildUserFromMap(Map<String, Object> data) {
        User user = new User();
        user.setFirstName((String) data.get("firstName"));
        user.setLastName((String) data.get("lastName"));
        user.setEmail((String) data.get("email"));
        user.setPassword((String) data.get("password"));
        Object ageObj = data.get("age");
        if (ageObj instanceof Integer age) {
            user.setAge(age);
        } else if (ageObj instanceof String strAge) {
            user.setAge(Integer.parseInt(strAge));
        }
        return user;
    }

    private List<Long> extractRoleIds(Map<String, Object> data) {
        List<?> raw = (List<?>) data.getOrDefault("roles", List.of());
        return raw.stream()
                .map(Object::toString)
                .map(Long::parseLong)
                .toList();
    }


    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}