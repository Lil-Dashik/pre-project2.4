package pre_project24.SpringSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pre_project24.SpringSecurity.model.User;
import pre_project24.SpringSecurity.service.AdminService;


import java.util.List;


@Controller
@RequestMapping("")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminPage(Model model, @AuthenticationPrincipal User userDetails) {
        return adminService.getAdminPage(model, userDetails);
    }

    @GetMapping("/admin/addUser")
    @PreAuthorize("hasRole('ADMIN')")
    public String showAddUserForm(Model model) {
        return adminService.showAddUserForm(model);
    }

    @PostMapping("/admin/addUser")
    @PreAuthorize("hasRole('ADMIN')")
    public String addUser(@ModelAttribute("user") User user,
                          @RequestParam("roles") List<Long> roleIds,
                          RedirectAttributes redirectAttributes) {
        return adminService.addUser(user, roleIds, redirectAttributes);
    }


    @GetMapping("/user/{id}")
    public String getUserById(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return adminService.getUserById(id, model, redirectAttributes);
    }

    @PostMapping("/admin/updateRoles")
    public String updateUserRoles(@RequestParam Long userId,
                                  @RequestParam(required = false) List<Long> roleIds) {
        return adminService.updateUser(userId, roleIds);
    }

    @GetMapping("/admin/getUser/{id}")
    @ResponseBody
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return adminService.getUser(id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    @GetMapping("/admin/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        return adminService.editUserForm(id, model);
    }

    @PostMapping("/admin/edit")
    public String editUser(@ModelAttribute("editUser") User editUser,
                           @RequestParam("roles") List<Long> roleIds,
                           RedirectAttributes redirectAttributes) {
       return adminService.editUser(editUser, roleIds, redirectAttributes);
    }

    @PostMapping("/admin/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@RequestParam("id") Long id, RedirectAttributes redirectAttributes) {
        return adminService.deleteUser(id, redirectAttributes);
    }
}