package pre_project24.SpringSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pre_project24.SpringSecurity.repository.UserRepository;

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
}
