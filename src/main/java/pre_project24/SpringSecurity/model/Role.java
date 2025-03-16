package pre_project24.SpringSecurity.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private RoleName role;
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public Role() {
    }

    public Role(RoleName role, Set<User> users) {
        this.role = role;
        this.users = users;
    }

    @Override
    public String toString() {
        return this.role.toString();
    }

    @Override
    public String getAuthority() {
        return this.role.name();
    }
}
