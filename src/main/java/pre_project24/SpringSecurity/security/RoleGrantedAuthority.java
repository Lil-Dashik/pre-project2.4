package pre_project24.SpringSecurity.security;

import org.springframework.security.core.GrantedAuthority;
import pre_project24.SpringSecurity.models.Role;

public class RoleGrantedAuthority implements GrantedAuthority {
    private final Role role;

    public RoleGrantedAuthority(Role role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.role.getRole().name();
    }
}
