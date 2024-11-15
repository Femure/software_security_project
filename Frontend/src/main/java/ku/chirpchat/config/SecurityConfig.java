package ku.chirpchat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import ku.chirpchat.component.handler.CustomLoginFailureHandler;
import ku.chirpchat.component.handler.CustomLoginSuccessHandler;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private OidcUserService oidcUserService;

        @Autowired
        private ApplicationContext context;

        @Autowired
        private CustomLoginFailureHandler loginFailureHandler;

        @Autowired
        private CustomLoginSuccessHandler loginSuccessHandler;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                .authorizeHttpRequests()
                                .antMatchers("/home", "/signup", "/verify", "/login", "/policy",
                                                "/resendTokenEmail*", "/signup-success", "/consent-form",
                                                "/forgot-password",
                                                "/reset-password", "/css/**", "/js/**", "/fontawesome/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()

                                .and()
                                .formLogin()
                                .loginPage("/login")
                                .failureHandler(loginFailureHandler)
                                .successHandler(loginSuccessHandler)
                                .permitAll()

                                .and()
                                .oauth2Login()
                                .defaultSuccessUrl("/thread", true)

                                .and()
                                .logout()
                                .logoutUrl("/logout")
                                .clearAuthentication(true)
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID", "remember-me")
                                .permitAll()

                                // headers options
                                // config CSP against XSS
                                .and()
                                .headers()
                                .xssProtection()
                                .and()
                                .frameOptions()
                                .deny()
                                .and()
                                .headers()
                                .httpStrictTransportSecurity()
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000)
                                .and()
                                .contentTypeOptions()
                                .and()
                                .cacheControl()
                                .and()
                                .contentSecurityPolicy(
                                                "default-src 'none'; frame-src https://www.google.com/; script-src 'self'; script-src-elem https://www.google.com/recaptcha/api.js https://www.gstatic.com/recaptcha/releases/ http://localhost:8090/js/bootstrap.min.js; connect-src 'self'; img-src 'self' data: ; style-src 'self'; frame-ancestors 'self'; form-action 'self'; font-src http://localhost:8090/fontawesome/webfonts/");

                ClientRegistrationRepository repository = context.getBean(ClientRegistrationRepository.class);

                if (repository != null) {
                        http
                                        .oauth2Login().clientRegistrationRepository(repository)
                                        .userInfoEndpoint().oidcUserService(oidcUserService).and()
                                        .loginPage("/login").permitAll();
                }

                return http.build();
        }

        @Bean
        public PasswordEncoder encoder() {
                return new BCryptPasswordEncoder(12);
        }

}
