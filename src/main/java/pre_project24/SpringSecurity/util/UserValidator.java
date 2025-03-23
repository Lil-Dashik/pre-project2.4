package pre_project24.SpringSecurity.util;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pre_project24.SpringSecurity.model.UserDTO;
import pre_project24.SpringSecurity.service.UserValidationService;

@Component
public class UserValidator implements Validator {
    private final UserValidationService userService;

    @Autowired
    public UserValidator(UserValidationService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if (userService.isUserExists(userDTO.getEmail())) {
            errors.rejectValue("email", "Duplicate.userForm.email", "Email already exists");
        }
    }
}
