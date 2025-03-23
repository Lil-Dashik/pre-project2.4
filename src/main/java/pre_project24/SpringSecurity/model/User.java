package pre_project24.SpringSecurity.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonManagedReference
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String firstName, String lastName, String password, Integer age, String email, Set<Role> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.age = age;
        this.email = email;
        this.roles = roles;
    }

    public User(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.password = user.getPassword();
        this.age = user.getAge();
        this.email = user.getEmail();
        this.roles = new HashSet<>(user.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
