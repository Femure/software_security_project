package ku.project.component.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ku.project.dto.SignupDto;
import ku.project.service.AuthService;

@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private AuthService authService;

    Logger logger = LoggerFactory.getLogger(CustomLoginSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();
        SignupDto user = authService.getMember(username);
        if (user.isAccountNonLocked()) {
            authentication.setAuthenticated(false);
            logger.warn("Fail login attempt for user " + username + "  at " + Instant.now() + ". Reason : Locked");
            throw new LockedException("Locked");
        } else {
            if (!user.isEnabled()) {
                authentication.setAuthenticated(false);
                logger.warn("Fail login attempt for user " + username + "  at " + Instant.now() + ". Reason : Disabled");
                throw new DisabledException("Disabled");
            } else {
                if (user.getFailedAttempt() > 0) {
                    authService.resetFailedAttempts(username);
                }
            }

        }

        if (authentication.isAuthenticated()) {
            logger.warn("Successfully login for user " + username + "  at " + Instant.now());
            super.setDefaultTargetUrl("/restaurant");
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}