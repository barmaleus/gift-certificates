package by.rekuts.giftcertificates.service;

import by.rekuts.giftcertificates.service.converter.CertificateConverter;
import by.rekuts.giftcertificates.service.converter.TagConverter;
import by.rekuts.giftcertificates.service.converter.UserConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan(basePackages = "by.rekuts.giftcertificates.service")
public class ServiceConfig {
    @Bean
    CertificateConverter getCertConverter() {
        return new CertificateConverter();
    }

    @Bean
    TagConverter getTagConverter() {
        return new TagConverter();
    }

    @Bean
    UserConverter getUserConverter() {
        return new UserConverter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
