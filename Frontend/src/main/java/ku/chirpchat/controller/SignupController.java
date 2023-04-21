package ku.chirpchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;

import ku.chirpchat.dto.ConsentDto;
import ku.chirpchat.dto.SignupDto;
import ku.chirpchat.service.SignupService;
import ku.chirpchat.validation.CaptchaValidator;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class SignupController {

    @Autowired
    private SignupService signupService;

    @Autowired
    private CaptchaValidator validator;

    @GetMapping("/signup")
    public String viewSignupPage(SignupDto user, Model model) {
        if (!model.containsAttribute("lastUser")) {
            model.addAttribute("lastUser", new SignupDto());
        }
        return "signup/signup";
    }

    @PostMapping("/signup")
    public String signupUser(@Valid SignupDto user, BindingResult result, HttpSession session,
            @RequestParam("g-recaptcha-response") String captcha, RedirectAttributes redirectAttributes,
            Model model) {
        if (result.hasErrors()) {
            // To keep the input field after error
            redirectAttributes.addFlashAttribute("lastUser", user);
            return "signup/signup";
        }

        String signupError = null;
        if (!signupService.isUsernameAvailable(user.getUsername())) {
            signupError = "The username already exists.";
            model.addAttribute("signupError", signupError);
        }

        if (!signupService.isEmailAvailable(user.getEmail())) {
            signupError = "The email adress already exists.";
            model.addAttribute("signupError", signupError);
        }

        if (validator.isValidCaptcha(captcha)) {
            if (signupError == null) {
                String token = signupService.createMember(user);
                session.setAttribute("tmpToken", token);
                session.setAttribute("username", user.getUsername());
                return "redirect:/consent-form";
            }
        } else {
            model.addAttribute("errorCaptcha", "Please validate reCaptcha");
        }

        return "signup/signup";

    }

    @GetMapping("/consent-form")
    public String viewConsenFormPage(ConsentDto consent, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }else{
            return "signup/consent-form";
        }
        
    }

    @PostMapping("/consent-form")
    public String consenForm(ConsentDto consent, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } else {
            signupService.createConsent(consent, username);
            session.removeAttribute("username");
            session.setAttribute("token", (String) session.getAttribute("tmpToken"));
            session.removeAttribute("tmpToken");
            return "redirect:/signup-success";
        }

    }

}
