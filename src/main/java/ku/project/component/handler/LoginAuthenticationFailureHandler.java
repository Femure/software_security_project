package ku.project.component.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {
        System.out.print("\nCAUSE="+exception.getCause());
        System.out.print("\nMESSAGE="+exception.getMessage());
        if (exception.getMessage().equals("Not verified")) {
            System.out.print("\n OKK \n");
            super.setDefaultFailureUrl("/login?notVerified");
        } else {
            super.setDefaultFailureUrl("/login?error");
        }
        super.onAuthenticationFailure(request, response, exception);
    }

}
