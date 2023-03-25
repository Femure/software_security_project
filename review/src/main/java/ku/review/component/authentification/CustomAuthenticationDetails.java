package ku.review.component.authentification;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import jakarta.servlet.http.HttpServletRequest;

public class CustomAuthenticationDetails extends WebAuthenticationDetails {

    public CustomAuthenticationDetails(HttpServletRequest request) {

        // TODO: (DONE login) This would be a good place to pull any extra parameters that we might
        // need to authenticate our user out of the HttpServletRequest.
        // We could store them in fields here and access them in an AuthenticationProvider
        super(request);
    }
}