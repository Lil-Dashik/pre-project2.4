package pre_project24.SpringSecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pre_project24.SpringSecurity.model.Role;
import pre_project24.SpringSecurity.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Set<Role> getRolesByIds(List<Long> roleIds) {
        return new HashSet<>(roleRepository.findAllById(roleIds));
    }
public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId).orElseThrow(()-> new RuntimeException("Role not found"));
}
}
