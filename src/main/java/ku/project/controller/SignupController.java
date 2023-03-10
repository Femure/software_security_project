package ku.project.controller;

import ku.project.model.Member;
import ku.project.service.SignupService;
import me.legrange.haveibeenpwned.HaveIBeenPwndApi;
import me.legrange.haveibeenpwned.HaveIBeenPwndException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    @Autowired
    private SignupService signupService;

    @GetMapping("/signup")
    public String getSignupPage() {
        return "signup"; // return signup.html
    }

    @PostMapping("/signup")
    public String signupMember(@ModelAttribute Member member, Model model) throws HaveIBeenPwndException {

        String signupError = null;

        // Try if the password select by the user appeared in password data breach
        HaveIBeenPwndApi hibp = me.legrange.haveibeenpwned.HaveIBeenPwndBuilder.create("Restaurant").build();

        boolean pwned = hibp.isPlainPasswordPwned(member.getPassword());
        if (pwned) {
            signupError = "Your password has previously appeared in a data breach. Change to a stronger password";
        }


        if (!signupService.isUsernameAvailable(member.getUsername())) {
            signupError = "The username already exists.";
        }

        if (signupError == null) {
            int rowsAdded = signupService.createMember(member);
            if (rowsAdded < 0) {
                signupError = "There was an error signing you up. Please try again.";
            }
        }

        if (signupError == null) {
            model.addAttribute("signupSuccess", true);
        } else {
            model.addAttribute("signupError", signupError);
        }

        return "signup";
    }
}
