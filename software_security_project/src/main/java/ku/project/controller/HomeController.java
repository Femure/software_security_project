package ku.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

   @RequestMapping("/home")
   public String getHomePage(Model model) {
       model.addAttribute("greeting", "Welcome to project Website");
       return "home";  // name of a html template: home.html
   }
   
   @RequestMapping("/policy")
   public String getPolicyPage() {
       return "policy";  // name of a html template: home.html
   }

   @RequestMapping("/")
   public String getRootPage() {
       return "redirect:/home"; 
   }
}
