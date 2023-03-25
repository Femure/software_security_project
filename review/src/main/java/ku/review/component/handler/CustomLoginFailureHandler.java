package ku.review.component.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import ku.review.dto.SignupRequest;
import ku.review.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

        SignupRequest user = authService.getMember(request.getParameter("username"));
        String username = request.getParameter("username");

        exception = new BadCredentialsException("Invalid username or password");
        System.out.println("\nError\n");
        if (user != null) {
            // if user exists
            if (!user.isAccountNonLocked()) {
                if (user.isEnabled()) {
                    if (user.getFailedAttempt() < AuthService.MAX_FAILED_ATTEMPTS - 1) {
                        // if the number of failed is inferior of MAX_FAILED_ATTEMPTS (3)
                        authService.increaseFailedAttempts(username);
                    } else {
                        authService.lock(username);
                        exception = new LockedException("Your account has been locked due to 3 failed attempts."
                                + " It will be unlocked after 24 hours.");
                    }
                } else {
                    exception = new DisabledException(
                            " The account is not verify. Please check your email to validate your account");
                }
            } else {
                exception = new LockedException("Your account has been locked due to 3 failed attempts."
                        + " It will be unlocked after 24 hours.");
                if (authService.unlockWhenTimeExpired(username)) {
                    exception = new LockedException("Your account has been unlocked. Please try to login again.");
                }
            }
        }
        super.setDefaultFailureUrl("/login?error");
        super.onAuthenticationFailure(request, response, exception);
    }

}
