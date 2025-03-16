package pre_project24.SpringSecurity.util;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.service.UserValidationService;

@Component
public class UserValidator implements Validator {
    private final UserValidationService userService;

    @Autowired
    public UserValidator(UserValidationService userService) {
        this.userService = userService;
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        User user = (User) target;
        if (userService.isUserExists(user.getUsername())) {
            errors.rejectValue("username", "Duplicate.userForm.username", "Username already exists");
        }
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return User.class.equals(clazz);
    }
}
