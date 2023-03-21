package ku.project.config;

import ku.project.component.authentification.CustomAuthenticationDetailsSource;
import ku.project.component.authentification.CustomAuthenticationProvider;
import ku.project.component.handler.LoginAuthenticationFailureHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
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

        @Autowired
        private CustomAuthenticationDetailsSource customAuthenticationDetailsSource;

        @Autowired
        private LoginAuthenticationFailureHandler failureHandler;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                .authorizeRequests()
                                .antMatchers("/home", "/signup", "/verify", "/login*",
                                                "/css/**", "/js/**")
                                .permitAll()
                                .anyRequest().authenticated()

                                .and()
                                .formLogin()
                                .loginPage("/login")
                                .defaultSuccessUrl("/restaurant", true)
                                .failureHandler(failureHandler)
                                .authenticationDetailsSource(customAuthenticationDetailsSource)
                                // .loginProcessingUrl("/login")
                                // .failureForwardUrl("/login")
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
                                .permitAll();

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
        public DaoAuthenticationProvider authenticationProvider(@Autowired UserDetailsService userDetailsService,
                        @Autowired PasswordEncoder passwordEncoder) {
                return new CustomAuthenticationProvider(userDetailsService, passwordEncoder);
        }

        @Bean
        public PasswordEncoder encoder() {
                return new BCryptPasswordEncoder(12);
        }

}

//PASSWORD ENCODER CIRCULAR CYCLE