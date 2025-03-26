package pre_project24.SpringSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pre_project24.SpringSecurity.model.Role;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.model.UserDTO;
import pre_project24.SpringSecurity.repository.RoleRepository;
import pre_project24.SpringSecurity.repository.UserRepository;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;


    @Autowired
    public AdminService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                        UserMapper userMapper, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;

    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toUserDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO addUser(UserDTO userDTO) {
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        User user = userMapper.fromUserDTO(userDTO);
        if (userDTO.getRoleIds() != null && !userDTO.getRoleIds().isEmpty()) {
            updateUserRoles(user, userDTO.getRoleIds());
        }
        user = userRepository.save(user);
        return userMapper.toUserDTO(user);
    }

    @Transactional
    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id).map(user -> {
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setAge(userDTO.getAge());
            user.setEmail(userDTO.getEmail());

            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            }

            if (userDTO.getRoleIds() != null && !userDTO.getRoleIds().isEmpty()) {
                updateUserRoles(user, userDTO.getRoleIds());
            }

            userRepository.save(user);

            return userMapper.toUserDTO(user);
        });
    }

    public void updateUserRoles(User user, List<Long> newRoleIds) {
        user.getRoles().clear();
        Set<Role> updatedRoles = new HashSet<>();
        for (Long roleId : newRoleIds) {
            Optional<Role> roleOpt = roleRepository.findById(roleId);
            if (roleOpt.isPresent()) {
                updatedRoles.add(roleOpt.get());
            } else {
                throw new RuntimeException("Роль с id " + roleId + " не найдена.");
            }
        }
        user.setRoles(updatedRoles);
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}