
package ku.project.controller;

import ku.project.dto.SignupDto;
import ku.project.service.AuthService;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String loginView(SignupDto user, Model model) {
        if (!model.containsAttribute("lastUser")) {
            model.addAttribute("lastUser", new SignupDto());
        }
        return "login";
    }

    @PostMapping("/login")
    public String signupUser(SignupDto user, RedirectAttributes redirectAttributes, Model model) {
        String loginError = null;
        SignupDto member = authService.getMember(user.getUsername());
        if (member == null || passwordEncoder.matches(user.getPassword(), member.getPassword()) == false) {
            loginError = "Invalid username or password";
        } else {
            if (!member.isEnabled()) {
                loginError = "The account is not verify. Please check your email to validate your account";
            }
        }
        if (loginError == null) {
            return "restaurant";
        } else {
            model.addAttribute("loginError", loginError);
            redirectAttributes.addFlashAttribute("lastUser", user);
        }
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
            HttpServletResponse response,
            Authentication auth) {

        if (auth != null) {
            new SecurityContextLogoutHandler()
                    .logout(request, response, auth);
        }
        // You can redirect wherever you want, but generally it's a good
        // practice to show the login screen again.
        return "redirect:/login?logout";
    }
}
