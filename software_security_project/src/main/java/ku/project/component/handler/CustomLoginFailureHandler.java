package ku.project.component.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import ku.project.dto.SignupDto;
import ku.project.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private AuthService authService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        SignupDto user = authService.getMember(request.getParameter("username"));
        String username = request.getParameter("username");

        exception = new BadCredentialsException("BadCredentials");
        if (user != null) {
            // if user exists
            if (!user.isAccountNonLocked()) {
                if (user.isEnabled()) {
                    if (user.getFailedAttempt() < AuthService.MAX_FAILED_ATTEMPTS - 1) {
                        // if the number of failed is inferior of MAX_FAILED_ATTEMPTS (3)
                        authService.increaseFailedAttempts(username);
                    } else {
                        authService.lock(username);
                        exception = new LockedException("Locked");
                    }
                } else {
                    exception = new DisabledException(
                            "Disabled");
                }
            } else {
                exception = new LockedException("Locked");
                if (authService.unlockWhenTimeExpired(username)) {
                    exception = new LockedException("Unlocked");
                }
            }
        }
        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }

}
