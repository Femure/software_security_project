package ku.review.component.handler;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import ku.review.dto.SignupRequest;
import ku.review.service.AuthService;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();
        SignupRequest user = authService.getMember(username);
        if (user.isAccountNonLocked()) {
            authentication.setAuthenticated(false);
            throw new LockedException("");

        } else {
            if (!user.isEnabled()) {
                authentication.setAuthenticated(false);
                throw new DisabledException("");
            } else {
                if (user.getFailedAttempt() > 0) {
                    authentication.setAuthenticated(true);
                    authService.resetFailedAttempts(username);
                }
            }

        }

        if (authentication.isAuthenticated()) {
            super.setDefaultTargetUrl("/restaurant");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}