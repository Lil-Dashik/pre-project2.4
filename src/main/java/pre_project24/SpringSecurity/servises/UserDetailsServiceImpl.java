package pre_project24.SpringSecurity.servises;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pre_project24.SpringSecurity.models.Role;
import pre_project24.SpringSecurity.models.User;
import pre_project24.SpringSecurity.repositories.RoleRepository;
import pre_project24.SpringSecurity.repositories.UserRepository;
import pre_project24.SpringSecurity.security.UsersDetailsImp;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(s);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UsersDetailsImp(user.get());
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.getRoles().size();
        }
        return users;
    }

    public void updateUserRoles(Long userId, List<Long> roleIds) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Set<Role> roles = new HashSet<>();
            if (roleIds != null) {
                roles.addAll(roleRepository.findAllById(roleIds));
            }
            user.setRoles(roles);
            userRepository.save(user);
        }
    }

    public void removeUserRoles(Long userId, List<Long> roleIds) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent() && roleIds != null) {
            User user = userOptional.get();
            user.getRoles().removeIf(role -> roleIds.contains(role.getId()));
            userRepository.save(user);
        }
    }
}

