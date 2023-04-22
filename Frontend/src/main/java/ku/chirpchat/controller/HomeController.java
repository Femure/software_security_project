package ku.chirpchat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String viewHomePage(Model model) {
        model.addAttribute("greeting", "Welcome to project Website");
        return "home"; // name of a html template: home.html
    }

    @GetMapping("/policy")
    public String viewPolicyPage() {
        return "policy"; // name of a html template: home.html
    }

    @GetMapping("/")
    public String viewRootPage() {
        return "redirect:/home";
    }
}
