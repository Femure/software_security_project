package ku.chirpchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ku.chirpchat.service.TokenService;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/forgot-password")
    public String viewForgotPassword(HttpSession session, Principal principal,
            RedirectAttributes attr, Model model) {
        if (principal != null) {
            attr.addFlashAttribute("forbidden", true);
            return "redirect:/home";
        } else {
            String valid = (String) session.getAttribute("valid");
            if (valid != null) {
                model.addAttribute("valid", true);
            }
            return "login/forgot-password";
        }

    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, HttpSession session, Principal principal,
            RedirectAttributes attr, Model model) {
        if (principal != null) {
            attr.addFlashAttribute("forbidden", true);
            return "redirect:/home";
        } else {
            if (email.contains("chirpchatcompany@gmail.com")) {
                model.addAttribute("error", "This email is restricted !");
            } else {
                String token = tokenService.forgotPassword(email);
                if (token.contains("accountNotfound")) {
                    model.addAttribute("error", "This email address does not exist !");
                } else if (token.contains("emailSentNumberExceeded")) {
                    model.addAttribute("emailSentNumberExceeded", "The number of email sent has been exceeded !");
                } else {
                    model.addAttribute("valid", true);
                    session.setAttribute("valid", "true");
                    model.addAttribute("emailSent",
                            "Request to reset password sent. Check your inbox for the reset link.");
                    session.setAttribute("token", token);
                }
            }
            return "login/forgot-password";
        }
    }

    @GetMapping("/signup-success")
    public String viewSignupSuccessPage(HttpSession session, Model model, RedirectAttributes attr) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            attr.addFlashAttribute("forbidden", true);
            return "redirect:/home";
        } else {
            return "signup/signup-success";
        }

    }

    @GetMapping("/resendTokenEmail")
    public String resendTokenEmail(HttpServletRequest request, HttpSession session, RedirectAttributes attr) {
        String token = (String) session.getAttribute("token");
        String referer = request.getHeader("Referer");
        if (token == null) {
            attr.addFlashAttribute("forbidden", true);
            return "redirect:/home";
        } else {
            String newToken;
            if (referer.contains("signup-success")) {
                newToken = tokenService.resendTokenEmail(token, 0);
            } else {
                newToken = tokenService.resendTokenEmail(token, 1);
            }
            if (newToken.matches(token)) {
                attr.addFlashAttribute("cooldownResendEmail", "Wait 2 minutes before resend another email.");
            } else if (newToken.matches("emailSentNumberExceeded")) {
                attr.addFlashAttribute("emailSentNumberExceeded", "The number of email sent has been exceeded!");
                session.removeAttribute("valid");
            } else if (newToken.matches("accountNotfound")) {
                attr.addFlashAttribute("accountNotfound",
                        "Your account can't be found ! It's likely been deleted because your registration time expired. Please, sign up again.");
            } else {
                session.setAttribute("token", newToken);
                attr.addFlashAttribute("emailResent", "Validation email resent");
            }
            return "redirect:" + referer;
        }
    }

    @GetMapping("/verify")
    public String verifyToken(@Param("code") String code, HttpSession session, RedirectAttributes attr) {
        if (code == null) {
            attr.addFlashAttribute("forbidden", true);
            return "redirect:/home";
        } else {
            int result = tokenService.verifyToken(code);
            session.removeAttribute("token");
            session.removeAttribute("valid");
            if (result == 0) {
                return "verify/verify-fail";
            } else if (result == 1) {
                return "verify/verify-success";
            } else {
                session.setAttribute("token", code);
                return "redirect:/reset-password";
            }
        }

    }

    @GetMapping("/verify-success")
    public String viewVerifySuccess() {
        return "verify/verify-success";
    }

}
