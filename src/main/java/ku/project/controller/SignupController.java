package ku.project.controller;

import ku.project.dto.SignupDto;
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
    public String getSignupPage(SignupDto user, Model model) {
        if (!model.containsAttribute("createAccountModel")) {
            model.addAttribute("createAccountModel", new SignupDto());
        }
        return "signup"; // return signup.html
    }

    @PostMapping("/signup")
    public String signupUser(@Valid SignupDto user, BindingResult result, HttpServletRequest request,
            @RequestParam("g-recaptcha-response") String captcha, RedirectAttributes redirectAttributes,
            Model model) throws UnsupportedEncodingException, MessagingException {
        if (result.hasErrors()) {
            // To keep the input field after error
            redirectAttributes.addFlashAttribute("createAccountModel", user);
            return "signup";
        }

        String signupError = null;

        if (!signupService.isUsernameAvailable(user.getUsername())) {
            signupError = "The username already exists.";
        }

        if (validator.isValidCaptcha(captcha)) {
            if (signupError == null) {
                model.addAttribute("signupSuccess", true);
                signupService.createMember(user, getSiteURL(request));
                return "signup_success";
            } else {
                model.addAttribute("signupError", signupError);
            }
        } else {
            model.addAttribute("errorCaptcha", "Please validate captcha first");
        }

        model.addAttribute("signupDto", new SignupDto());
        return "signup";

    }

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
