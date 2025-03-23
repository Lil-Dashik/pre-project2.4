package pre_project24.SpringSecurity.service;

import org.springframework.stereotype.Component;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.model.UserDTO;

@Component
public class UserMapper {
    public User fromUserDTO(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        user.setAge(userDTO.getAge());
        user.setEmail(userDTO.getEmail());
        return user;
    }
}
