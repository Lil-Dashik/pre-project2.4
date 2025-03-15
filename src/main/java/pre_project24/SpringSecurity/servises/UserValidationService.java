package pre_project24.SpringSecurity.servises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pre_project24.SpringSecurity.models.User;
import pre_project24.SpringSecurity.repositories.UserRepository;

@Service
public class UserValidationService {
    private final UserRepository userRepository;

    @Autowired
    public UserValidationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUserExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public void registerUser(User user) {
        if (isUserExists(user.getUsername())) {
            throw new IllegalArgumentException("User with this name already exists!");
        }
        userRepository.save(user);
    }
}
