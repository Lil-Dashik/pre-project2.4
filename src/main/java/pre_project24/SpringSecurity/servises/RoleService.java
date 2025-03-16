package pre_project24.SpringSecurity.servises;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pre_project24.SpringSecurity.models.Role;
import pre_project24.SpringSecurity.repositories.RoleRepository;

import java.util.List;

@Service
public class RoleService {
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
