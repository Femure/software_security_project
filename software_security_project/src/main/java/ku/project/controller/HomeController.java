package ku.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String getHomePage(Model model) {
        model.addAttribute("greeting", "Welcome to project Website");
        return "home"; // name of a html template: home.html
    }

    @GetMapping("/policy")
    public String getPolicyPage() {
        return "policy"; // name of a html template: home.html
    }

    @GetMapping("/")
    public String getRootPage() {
        return "redirect:/home";
    }
}
