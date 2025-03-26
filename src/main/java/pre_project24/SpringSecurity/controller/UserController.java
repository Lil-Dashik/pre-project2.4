package pre_project24.SpringSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pre_project24.SpringSecurity.model.UserDTO;
import pre_project24.SpringSecurity.service.UserService;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public ResponseEntity<?> getUserInfo(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }
        UserDTO userDTO = userService.findByEmailDTO(principal.getName());
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsersDTO() {
        List<UserDTO> userDTOs = userService.getAllUsersDTO();
        return ResponseEntity.ok(userDTOs);
    }
}

