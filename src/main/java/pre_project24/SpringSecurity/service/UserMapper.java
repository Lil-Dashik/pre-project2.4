package pre_project24.SpringSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pre_project24.SpringSecurity.model.Role;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.model.UserDTO;
import pre_project24.SpringSecurity.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private final RoleRepository roleRepository;

    @Autowired
    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public User fromUserDTO(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        user.setAge(userDTO.getAge());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getRoleIds() != null && !userDTO.getRoleIds().isEmpty()) {
            Set<Role> roles = new HashSet<>();
            for (Long roleId : userDTO.getRoleIds()) {
                Optional<Role> roleOpt = roleRepository.findById(roleId);
                roleOpt.ifPresent(roles::add);
            }
            user.setRoles(roles);
        }

        return user;
    }

    public UserDTO toUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAge(user.getAge());
        List<Long> roleIds = user.getRoles().stream()
                .map(role -> role.getId())
                .collect(Collectors.toList());
        userDTO.setRoleIds(roleIds);
        return userDTO;
    }
}
