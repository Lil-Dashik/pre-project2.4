package pre_project24.SpringSecurity.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO {
    private Long id;
    private String role;

    public RoleDTO() {
    }

    public RoleDTO(Long id, String role) {
        this.id = id;
        this.role = role;
    }
}
