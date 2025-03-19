package pre_project24.SpringSecurity.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import pre_project24.SpringSecurity.model.Role;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.repository.RoleRepository;
import pre_project24.SpringSecurity.repository.UserRepository;

import java.util.*;

import static pre_project24.SpringSecurity.model.RoleName.ROLE_USER;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(s);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new User(user.get());
    }
    public void prepareUserPage(Model model, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("page", user);
        model.addAttribute("pageTitle", "User Page");
        model.addAttribute("user", user);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.getRoles().size();
        }
        return users;
    }
    public void usersListPage(Model model){
        List<User> users = userRepository.findAll();
        model.addAttribute("users", users);
    }

    @Transactional
    public void addUserWithRoles(User user, List<Long> roleIds) {
        Set<Role> roles = new HashSet<>(roleService.getRolesByIds(roleIds));
        user.setRoles(roles);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void updateUserRoles(Long userId, List<Long> roleIds) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (roleIds == null || roleIds.isEmpty()) {
                Role defaultRole = roleRepository.findByRole(ROLE_USER).get();
                user.setRoles(Set.of(defaultRole));
            } else {
                Set<Role> updateRoles = new HashSet<>(roleRepository.findAllById(roleIds));
                if (updateRoles.isEmpty()) {
                    Role defaultRole = roleRepository.findByRole(ROLE_USER).get();
                    updateRoles.add(defaultRole);
                }
                user.setRoles(updateRoles);
            }
            userRepository.save(user);
        }
    }
}

