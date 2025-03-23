package pre_project24.SpringSecurity.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDTO {
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String firstName;
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String lastName;
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 3000, message = "Password should be between 2 and 30 characters")
    private String password;
    @NotNull(message = "Age should not be empty")
    @Min(value = 3, message = "Age should be greater than 3")
    private Integer age;
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    private String email;
    private List<Long> roleIds;
}
