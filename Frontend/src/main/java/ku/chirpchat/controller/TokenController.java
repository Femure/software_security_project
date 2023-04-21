package ku.chirpchat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import ku.chirpchat.service.TokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/forgot-password")
    public String viewForgotPassword(HttpSession session, Model model) {
        String valid = (String) session.getAttribute("valid");
        if (valid != null) {
            model.addAttribute("valid", true);
            String message = (String) session.getAttribute("message");
            if (message != null) {
                if (message.matches("emailCooldown")) {
                    model.addAttribute("cooldownResendEmail", "Wait 2 minutes before resend another email.");
                } else if (message.matches("emailSentNumberExceeded")) {
                    model.addAttribute("emailSentNumberExceeded", "The number of email sent has been exceeded!");
                } else {
                    model.addAttribute("emailResent", "Reset password email resent");
                }
            }
        }
        return "login/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam("email") String email, HttpSession session, Model model) {
        String token = tokenService.forgotPassword(email);
        if (token != null) {
            model.addAttribute("valid", true);
            model.addAttribute("emailSent", "Request to reset password sent. Check your inbox for the reset link.");
            session.setAttribute("token", token);
            session.setAttribute("valid", "true");
        } else {
            model.addAttribute("error", "This email address does not exist!");
        }
        return "login/forgot-password";
    }

    @GetMapping("/signup-success")
    public String signupSuccessPage(HttpSession session, Model model) {
        String token = (String) session.getAttribute("token");
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } else {
            String message = (String) session.getAttribute("message");
            if (message != null) {
                if (message.matches("emailCooldown")) {
                    model.addAttribute("cooldownResendEmail", "Wait 2 minutes before resend another email.");
                } else if (message.matches("emailSentNumberExceeded")) {
                    model.addAttribute("emailSentNumberExceeded", "The number of email sent has been exceeded!");
                } else {
                    model.addAttribute("emailResent", "Validation email resent");
                }
            }
            return "signup/signup-success";
        }

    }

    @GetMapping("/resendTokenEmail")
    public String resendTokenEmail(HttpServletRequest request, HttpSession session) {
        String token = (String) session.getAttribute("token");
        String referer = request.getHeader("Referer");
        if (token == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        } else {
            String newToken;
            if (referer.contains("signup-success")) {
                newToken = tokenService.resendTokenEmail(token, 0);
            } else {
                newToken = tokenService.resendTokenEmail(token, 1);
            }
            if (newToken.matches(token)) {
                session.setAttribute("message", "emailCooldown");
            } else if (newToken.matches("emailSentNumberExceeded")) {
                session.setAttribute("message", "emailSentNumberExceeded");
            } else {
                session.setAttribute("token", newToken);
                session.setAttribute("message", "emailSent");
            }
            return "redirect:" + referer;
        }
    }

    @GetMapping("/verify")
    public String verifyToken(@Param("code") String code, HttpSession session) {
        int result = tokenService.verifyToken(code);
        session.removeAttribute("message");
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

    @GetMapping("/verify-success")
    public String verifySuccess() {
        return "verify/verify-success";
    }

}
