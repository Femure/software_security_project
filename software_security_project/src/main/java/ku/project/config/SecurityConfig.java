package ku.project.config;

import ku.project.component.handler.CustomLoginSuccessHandler;
import ku.project.component.handler.CustomLoginFailureHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private OidcUserService oidcUserService;

        @Autowired
        private ApplicationContext context;

        // @Autowired
        // private CustomAuthenticationDetailsSource customAuthenticationDetailsSource;

        @Autowired
        private CustomLoginFailureHandler loginFailureHandler;

        @Autowired
        private CustomLoginSuccessHandler loginSuccessHandler;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                .authorizeRequests()
                                .antMatchers("/home", "/signup", "/verify", "/login", "/policy*",
                                                "/resendTokenEmail*", "/signup-success", "/forgot-password",
                                                "/reset-password", "/css/**", "/js/**")
                                .permitAll()
                                // .antMatchers("/restaurant", "/restaurant/**", "/review", "/review/**")
                                // .access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                                // .antMatchers("/restaurant/add")
                                // .access("hasRole('ROLE_ADMIN')")

                                .anyRequest().authenticated()

                                .and()
                                .formLogin()
                                .loginPage("/login")
                                .failureHandler(loginFailureHandler)
                                .successHandler(loginSuccessHandler)
                                // .authenticationDetailsSource(customAuthenticationDetailsSource)
                                .permitAll()

                                .and()
                                .oauth2Login()
                                .defaultSuccessUrl("/restaurant", true)

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
                                                "default-src 'none'; frame-src https://www.google.com/; script-src 'self'; script-src-elem https://www.google.com/recaptcha/api.js https://www.gstatic.com/recaptcha/releases/ http://localhost:8090/js/bootstrap.min.js; connect-src 'self'; img-src 'self'; style-src 'self'; frame-ancestors 'self'; form-action 'self';");

                ClientRegistrationRepository repository = context.getBean(ClientRegistrationRepository.class);

                if (repository != null) {
                        http
                                        .oauth2Login().clientRegistrationRepository(repository)
                                        .userInfoEndpoint().oidcUserService(oidcUserService).and()
                                        .loginPage("/login").permitAll();
                }

                return http.build();
        }

        // @Bean
        // public DaoAuthenticationProvider authenticationProvider(@Autowired
        // UserDetailsService userDetailsService,
        // @Autowired PasswordEncoder passwordEncoder) {
        // return new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
        // }

        @Bean
        public PasswordEncoder encoder() {
                return new BCryptPasswordEncoder(12);
        }

}
