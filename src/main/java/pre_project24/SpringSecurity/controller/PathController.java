package pre_project24.SpringSecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PathController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/dashboard.html";
    }



}
