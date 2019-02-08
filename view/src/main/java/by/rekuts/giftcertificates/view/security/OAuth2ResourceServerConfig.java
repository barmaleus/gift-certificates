package by.rekuts.giftcertificates.view.security;

import by.rekuts.giftcertificates.view.exceptionhandlers.RestAccessDeniedHandler;
import by.rekuts.giftcertificates.view.exceptionhandlers.RestOauth2EntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    public static final String CSRF_TOKEN = "CSRF-TOKEN";
    public static final String X_CSRF_TOKEN = "X-CSRF-TOKEN";
    private static final String RESOURCE_ID = "resource_id";

    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
        config.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        http
                .requiresChannel()
                .anyRequest().requiresSecure();

        http
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/tags", "/tags/**", "/users", "/users/**")
                        .access("#oauth2.hasScope('read')")    //todo buy certs

                .antMatchers(HttpMethod.GET, "/certificates", "/certificates/**").permitAll()

                .antMatchers(HttpMethod.POST, "/certificates", "/tags")
                        .access("#oauth2.hasScope('write')")

                .antMatchers(HttpMethod.POST, "/users").permitAll()

                .antMatchers(HttpMethod.DELETE, "/certificates/**", "/tags/**", "/users/**")
                        .access("#oauth2.hasScope('write')")

                .antMatchers(HttpMethod.PUT, "/certificates/**", "/tags/**", "/users/**")
                        .access("#oauth2.hasScope('write')")

                .anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedHandler(oauthAccessDeniedHandler())
                .and().exceptionHandling().authenticationEntryPoint(oAuth2AuthenticationEntryPoint());
    }

    //works when user is not authorized
    @Bean
    public AuthenticationEntryPoint oAuth2AuthenticationEntryPoint() {
        return new RestOauth2EntryPoint();
    }

    //works when user is not an ADMIN and tries to get admin resources ("/user/**)
    @Bean
    public OAuth2AccessDeniedHandler oauthAccessDeniedHandler() {
        return new RestAccessDeniedHandler();
    }
}