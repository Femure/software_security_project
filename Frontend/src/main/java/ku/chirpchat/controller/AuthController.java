
package ku.chirpchat.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginView(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        String loginError = null;
        if (session != null) {
            AuthenticationException exception = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (exception != null) {
                switch (exception.getMessage()) {
                    case "BadCredentials":
                        loginError = "Invalid username or password";
                        break;
                    case "Locked":
                        loginError = "Your account has been locked due to 3 failed attempts. It will be unlocked after 24 hours.";
                        break;
                    case "Disabled":
                        loginError = "The account is not verify. Please check your email to validate your account";
                        break;
                    case "Unlocked":
                        loginError = "Your account has been unlocked. Please try to login again.";
                        break;
                    default:
                        break;
                }
            }

            model.addAttribute("loginError", loginError);
        }
        return "login/login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
            HttpServletResponse response,
            Authentication auth, RedirectAttributes attr) {
        String referer = request.getHeader("Referer");
        if (auth != null) {
            new SecurityContextLogoutHandler()
                    .logout(request, response, auth);
        }
        if (referer.contains("delete-account")) {
            attr.addFlashAttribute("deleteSuccess", true);
            return "redirect:/login";
        }
        // You can redirect wherever you want, but generally it's a good
        // practice to show the login screen again.
        return "redirect:/login?logout";
    }
}
