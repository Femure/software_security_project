package ku.project.controller;

import ku.project.model.Member;
import ku.project.service.SignupService;
import me.legrange.haveibeenpwned.HaveIBeenPwndApi;
import me.legrange.haveibeenpwned.HaveIBeenPwndException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class SignupController implements WebMvcConfigurer {

    @Autowired
    private SignupService signupService;

    @GetMapping("/signup")
    public String getSignupPage() {
        return "signup"; // return signup.html
    }

    @PostMapping("/signup")
    public String signupMember(@ModelAttribute Member member, Model model) throws HaveIBeenPwndException {
        String signupError = null;
        String passwordError = null;
        int signupSuccess = 0;

        // Try if the password select by the user appeared in password data breach
        HaveIBeenPwndApi hibp = me.legrange.haveibeenpwned.HaveIBeenPwndBuilder.create("Restaurant").build();

        boolean pwned = hibp.isPlainPasswordPwned(member.getPassword());
        if (pwned) {
            passwordError = "Your password has previously appeared in a data breach. Change to a stronger password";
        }

        // Compare if the confirmation password and the password are the same
        System.out.printf("%s\n", member.getConfirmationPassword());

        if (member.getConfirmationPassword() != member.getPassword()) {
            passwordError = "Your confirmation password is not identical to your password";
        }

        if (member.getPassword().length() < 12) {
            passwordError = "Your password need to contain at least 12 characters";
        }

        if (!signupService.isUsernameAvailable(member.getUsername())) {
            signupError = "The username already exists.";
            
        }

        if (signupError == null) {
            signupSuccess += 1;
        } else {
            model.addAttribute("signupError", signupError);
        }

        if (passwordError == null) {
            signupSuccess += 1;
        } else {
            model.addAttribute("passwordError", passwordError);
        }

        if (signupSuccess == 2) {
            model.addAttribute("signupSuccess", true);
            int rowsAdded = signupService.createMember(member);
            if (rowsAdded < 0) {
                signupError = "There was an error signing you up. Please try again.";
            }
        } 

        return "signup";
    }
}
