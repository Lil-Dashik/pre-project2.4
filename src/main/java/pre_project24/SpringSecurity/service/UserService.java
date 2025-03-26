package pre_project24.SpringSecurity.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.model.UserDTO;
import pre_project24.SpringSecurity.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper) {

        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDTO> getAllUsersDTO() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> userMapper.toUserDTO(user))
                .collect(Collectors.toList());
    }

    public UserDTO findByEmailDTO(String email) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user == null) {
            return null;
        }
        return userMapper.toUserDTO(user);
    }
}
