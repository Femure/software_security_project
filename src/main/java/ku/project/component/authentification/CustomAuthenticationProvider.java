package ku.project.component.authentification;

import ku.project.dto.SignupDto;
import ku.project.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        super();
        this.setUserDetailsService(userDetailsService);
        this.setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SignupDto user = authService.getMember(authentication.getName());
        String password = (String) authentication.getCredentials();

        System.out.print("Before\n");
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                try {
                    if (!user.isEnabled()) {
                        throw new BadCredentialsException("Not verified");
                    }

                } catch (Exception e) {
                    throw new AuthenticationServiceException("Not verified");
                }
            }

        }

        return super.authenticate(authentication);
    }
}
