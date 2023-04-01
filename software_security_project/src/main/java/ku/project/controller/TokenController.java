package ku.project.controller;

import ku.project.dto.SignupDto;
import ku.project.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import javax.servlet.http.HttpSession;

@Controller
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/forgot-password")
    public String viewForgotPassword(SignupDto user) {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@Valid SignupDto user, BindingResult result, HttpServletRequest request, Model model) {
        if (result.hasFieldErrors("email")) {
            return "forgot-password";
        }
        boolean resp = tokenService.forgotPassword(user.getEmail());
        if (resp) {
            model.addAttribute("valid", "Request to reset password sent. Check your inbox for the reset link.");
        } else {
            model.addAttribute("error", "This email address does not exist!");
        }
        return "forgot-password";
    }

    @GetMapping("/reset-password")
    public String viewResetPassword(SignupDto user) {
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@Valid SignupDto user, BindingResult result, HttpServletRequest request,
            HttpSession session, Model model) {
        if (result.hasFieldErrors("password") || result.hasFieldErrors("confirmPassword")) {
            return "reset-password";
        }
        String token = (String) request.getSession().getAttribute("token");
        if (token == null) {
            model.addAttribute("error", "Please authentificate you by passing by reset password link sent to you.");
        } else {
            int resp = tokenService.resetPassword(user.getPassword(), token);
            if (resp == 0) {
            } else if (resp == 1) {
                model.addAttribute("error", "Select a password different from the previous!");
            } else {
                session.removeAttribute("token");
                model.addAttribute("valid",
                        "Your password has been successfully changed. Go to login page to connect you.");
            }
        }
        return "reset-password";
    }

    @GetMapping("/signup-success")
    public String signupSuccessPage(@Param("code") String code, HttpServletRequest request, Model model) {
        String referer = request.getHeader("Referer");
        if (referer != null) {
            if (referer.length() > 42) {
                String previousCode = referer.substring(42, referer.length());
                if (previousCode.matches(code)) {
                    String cooldownResendEmail = "Wait 2 minutes before resend another email";
                    model.addAttribute("cooldownResendEmail", cooldownResendEmail);
                } else {
                    String emailResent = "Validation email resent";
                    model.addAttribute("emailResent", emailResent);
                }
            }
            return "signup-success";
        }
        return "redirect:/login";
    }

    @GetMapping("/resendValidationEmail")
    public String resendVerificationEmail(@Param("code") String code) {
        String newCode = tokenService.resendTokenEmail(code);
        return "redirect:/signup-success?code=" + newCode;
    }

    @GetMapping("/verify")
    public String verifyToken(@Param("code") String code, HttpSession session) {
        int result = tokenService.verifyToken(code);
        if (result == 0) {
            return "verify-fail";
        } else if (result == 1) {
            return "verify-success";
        } else {
            session.setAttribute("token", code);
            return "redirect:/reset-password";
        }
    }

    @GetMapping("/verify-success")
    public String verifySuccess() {
        return "verify-success";
    }

}
