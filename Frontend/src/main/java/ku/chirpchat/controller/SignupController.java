package ku.chirpchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ku.chirpchat.dto.ConsentDto;
import ku.chirpchat.dto.SignupDto;
import ku.chirpchat.service.SignupService;
import ku.chirpchat.validation.CaptchaValidator;

import java.security.Principal;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class SignupController {

    @Autowired
    private SignupService signupService;

    @Autowired
    private CaptchaValidator validator;

    @GetMapping("/signup")
    public String viewSignupPage(SignupDto user,Principal principal,
    RedirectAttributes attr,  Model model) {
        if (principal != null) {
            attr.addFlashAttribute("forbidden", true);
            return "redirect:/home";
        } else {
            if (!model.containsAttribute("signupDto")) {
                model.addAttribute("signupDto", new SignupDto());
            }
            return "signup/signup";
        }

    }

    @PostMapping("/signup")
    public String signupUser(@Valid SignupDto user, BindingResult result, HttpSession session,
            @RequestParam("g-recaptcha-response") String captcha, RedirectAttributes attr, Principal principal,
            Model model) {
        if (principal != null) {
            attr.addFlashAttribute("forbidden", true);
            return "redirect:/home";
        } else {
            if (result.hasErrors()) {
                attr.addFlashAttribute("signupDto", user);
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
                    session.setAttribute("user", user);
                    return "redirect:/consent-form";
                }
            } else {
                model.addAttribute("errorCaptcha", "Please validate reCaptcha");
            }

            return "signup/signup";
        }

    }

    @GetMapping("/consent-form")
    public String viewConsenFormPage(ConsentDto consent, HttpSession session, RedirectAttributes attr) {
        SignupDto user = (SignupDto) session.getAttribute("user");
        if (user == null) {
            attr.addFlashAttribute("forbidden", true);
            return "redirect:/home";
        } else {
            return "signup/consent-form";
        }

    }

    @PostMapping("/consent-form")
    public String consenForm(ConsentDto consent, HttpSession session, RedirectAttributes attr) {
        SignupDto user = (SignupDto) session.getAttribute("user");
        if (user == null) {
            attr.addFlashAttribute("forbidden", true);
            return "redirect:/home";
        } else {
            signupService.createConsent(consent, user.getUsername());
            String token = signupService.createMember(user);
            session.removeAttribute("user");
            session.setAttribute("token", token);
            return "redirect:/signup-success";
        }

    }

}
