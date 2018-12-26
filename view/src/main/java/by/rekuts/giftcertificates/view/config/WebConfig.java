package by.rekuts.giftcertificates.view.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"by.rekuts.giftcertificates.view", "by.rekuts.giftcertificates.service"})
public class WebConfig extends WebMvcConfigurerAdapter {

}