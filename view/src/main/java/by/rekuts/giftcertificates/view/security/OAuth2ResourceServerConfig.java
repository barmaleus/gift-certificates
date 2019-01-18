package by.rekuts.giftcertificates.view.security;

import by.rekuts.giftcertificates.view.exceptionhandlers.RestAccessDeniedHandler;
import by.rekuts.giftcertificates.view.exceptionhandlers.RestOauth2EntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String RESOURCE_ID = "resource_id";

    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
        config.resourceId(RESOURCE_ID);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().addFilterBefore(
                new StatelessCsrfFilter(), CsrfFilter.class);

        http
                .requiresChannel()
                .anyRequest().requiresSecure();

        http.
                httpBasic().disable()
                .formLogin().disable()
                .logout().disable()
                .anonymous().disable()
                .authorizeRequests()
                .antMatchers("/user", "/user/**").hasAuthority("ADMIN")
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