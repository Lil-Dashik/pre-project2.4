package pre_project24.SpringSecurity.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PathController {

//    @GetMapping("/registration")
//    public String registration() {
//        return "forward:/registration.html";  // Spring автоматически найдет registration.html в папке static
//    }
    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/dashboard.html";
    }



}
