package by.rekuts.giftcertificates.view.config;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected WebApplicationContext createRootApplicationContext() {
        WebApplicationContext context = super.createRootApplicationContext();
        ((ConfigurableEnvironment)context.getEnvironment()).setActiveProfiles("release");
        return context;
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { WebConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

}
