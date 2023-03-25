package ku.project.controller;

import ku.project.dto.SignupRequest;
import ku.project.service.SignupService;
import ku.project.validation.CaptchaValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class SignupController {

    @Autowired
    private SignupService signupService;

    @Autowired
    private CaptchaValidator validator;

    @GetMapping("/signup")
    public String getSignupPage(SignupRequest user, Model model) {
        if (!model.containsAttribute("lastUser")) {
            model.addAttribute("lastUser", new SignupRequest());
        }
        return "signup"; // return signup.html
    }

    @PostMapping("/signup")
    public String signupUser(@Valid SignupRequest user, BindingResult result, HttpServletRequest request,
            @RequestParam("g-recaptcha-response") String captcha, RedirectAttributes redirectAttributes,
            Model model) throws UnsupportedEncodingException, MessagingException {
        if (result.hasErrors()) {
            // To keep the input field after error
            redirectAttributes.addFlashAttribute("lastUser", user);
            return "signup";
        }

        String signupError = null;
        if (!signupService.isUsernameAvailable(user.getUsername())) {
            signupError = "The username already exists.";
        }

        // if (!signupService.isEmailAvailable(user.getEmail())) {
        //     signupError = "The email adress already exists.";
        // }

        if (validator.isValidCaptcha(captcha)) {
            if (signupError == null) {
                signupService.createMember(user);
                redirectAttributes.addFlashAttribute("lastUser", "");
                model.addAttribute("signupEmail", true);
                return "signup_success";
            } else {
                model.addAttribute("signupError", signupError);
            }
        } else {
            model.addAttribute("errorCaptcha", "Please validate captcha first");
        }
        return "signup";

    }

    // @GetMapping("/resendValidationEmail")
    // public String resendValidationEmail(SignupDto user, Model model)
    //         throws UnsupportedEncodingException, MessagingException {
    //     String signupSuccess = "Validation email resent";
    //     model.addAttribute("signupSuccess", signupSuccess);
    //     signupService.resendVerificationEmail(user.getEmail(), "http://localhost");
    //     model.addAttribute("signupEmail", true);
    //     return "signup";
    // }

    @GetMapping("/verify")
    public String verifyMember(@Param("code") String code) {
        if (signupService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
