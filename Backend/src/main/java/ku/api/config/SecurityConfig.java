package ku.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Value("${auth0.audience}")
        private String audience;

        @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
        private String issuer;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests()
                                .requestMatchers(HttpMethod.GET, "/api/post")
                                .hasAuthority("SCOPE_read:posts")
                                .requestMatchers(HttpMethod.GET, "/api/comment")
                                .hasAuthority("SCOPE_read:comments")
                                .requestMatchers(HttpMethod.POST, "/api/post")
                                .hasAuthority("SCOPE_create:posts")
                                .requestMatchers(HttpMethod.POST, "/api/comment")
                                .hasAuthority("SCOPE_create:comments")
                                .requestMatchers(HttpMethod.DELETE, "/api/post")
                                .hasAuthority("SCOPE_delete:posts")
                                .requestMatchers(HttpMethod.DELETE, "/api/comment")
                                .hasAuthority("SCOPE_delete:comments")
                                .anyRequest()
                                .authenticated()
                                
                                .and()
                                .sessionManagement(management -> management
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .oauth2ResourceServer(server -> server
                                                .jwt()
                                                .decoder(jwtDecoder()));

                return http.build();
        }

        private JwtDecoder jwtDecoder() {
                OAuth2TokenValidator<Jwt> withAudience = new AudienceValidator(audience);

                OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);

                OAuth2TokenValidator<Jwt> validator = new DelegatingOAuth2TokenValidator<>(withAudience, withIssuer);

                NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation(issuer);

                jwtDecoder.setJwtValidator(validator);

                return jwtDecoder;
        }

}
