package by.rekuts.giftcertificates.service;

import by.rekuts.giftcertificates.repository.RepositoryConfig;
import by.rekuts.giftcertificates.service.converter.CertificateConverter;
import by.rekuts.giftcertificates.service.converter.TagConverter;
import org.springframework.context.annotation.*;

@Configuration
@Profile("release")
@Import(RepositoryConfig.class)
@ComponentScan (basePackages = "by.rekuts.giftcertificates.service")
public class ServiceConfig {
    @Bean
    CertificateConverter getCertConverter() {
        return new CertificateConverter();
    }

    @Bean
    TagConverter getTagConverter() {
        return new TagConverter();
    }
}
