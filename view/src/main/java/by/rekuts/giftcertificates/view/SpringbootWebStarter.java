package by.rekuts.giftcertificates.view;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

@Profile("release")
@SpringBootApplication(scanBasePackages = {"by.rekuts.giftcertificates"} )
public class SpringbootWebStarter {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootWebStarter.class, args);
    }
}
