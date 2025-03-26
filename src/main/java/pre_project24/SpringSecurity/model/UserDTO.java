package pre_project24.SpringSecurity.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class UserDTO {
    @JsonProperty("id")
    private Long id;

    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    @JsonProperty("firstName")
    private String firstName;

    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    @JsonProperty("lastName")
    private String lastName;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 3000, message = "Password should be between 2 and 30 characters")
    @JsonProperty("password")
    private String password;

    @NotNull(message = "Age should not be empty")
    @Min(value = 3, message = "Age should be greater than 3")
    @JsonProperty("age")
    private Integer age;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    @Column(unique = true)
    @JsonProperty("email")
    private String email;

    @JsonProperty("roleIds")
    private List<Long> roleIds;

    private String rolesString;

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String lastName, String password, Integer age, String email, List<Long> roleIds) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.age = age;
        this.email = email;
        this.roleIds = roleIds;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", roleIds=" + roleIds +
                '}';
    }

}
