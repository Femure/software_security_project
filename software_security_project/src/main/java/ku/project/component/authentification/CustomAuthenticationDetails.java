package ku.project.component.authentification;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class CustomAuthenticationDetails extends WebAuthenticationDetails {

    public CustomAuthenticationDetails(HttpServletRequest request) {

        super(request);
    }
}