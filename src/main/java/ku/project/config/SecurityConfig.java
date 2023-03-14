package ku.project.config;

import ku.project.service.UserDetailsServiceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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


        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http
                                .authorizeRequests()
                                .antMatchers("/home", "/signup",
                                                "/css/**", "/js/**")
                                .permitAll()
                                .anyRequest().authenticated()

                                .and()
                                .formLogin()
                                .loginPage("/login")
                                .defaultSuccessUrl("/restaurant", true)
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
        public PasswordEncoder encoder() {
                return new BCryptPasswordEncoder(12);
        }

}