package ku.project.controller;

import ku.project.dto.SignupDto;
import ku.project.service.SignupService;
import ku.project.validation.CaptchaValidator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        if (!model.containsAttribute("lastUser")) {
            model.addAttribute("lastUser", new SignupDto());
        }
        return "signup"; // return signup.html
    }

    @PostMapping("/signup")
    public String signupUser(@Valid SignupDto user, BindingResult result, HttpServletRequest request,
            @RequestParam("g-recaptcha-response") String captcha, RedirectAttributes redirectAttributes,
            Model model) {
        if (result.hasErrors()) {

            String signupError = null;
            if (!signupService.isUsernameAvailable(user.getUsername())) {
                signupError = "The username already exists.";
                model.addAttribute("signupError", signupError);
            }

            // if (!signupService.isEmailAvailable(user.getEmail())) {
            // signupError = "The email adress already exists.";
            // model.addAttribute("signupError", signupError);
            // }

            if (validator.isValidCaptcha(captcha)) {
                if (signupError == null) {
                    String code = signupService.createMember(user);
                    model.addAttribute("signupEmail", true);
                    return "redirect:/signup-success?code=" + code;
                }
            } else {
                model.addAttribute("errorCaptcha", "Please validate reCaptcha");
            }
            // To keep the input field after error
            redirectAttributes.addFlashAttribute("lastUser", user);
            return "signup";
        }

        return "signup";

    }

}