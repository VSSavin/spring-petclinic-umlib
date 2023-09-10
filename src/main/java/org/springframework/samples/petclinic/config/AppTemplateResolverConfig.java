package org.springframework.samples.petclinic.config;

import com.github.vssavin.umlib.config.UmTemplateResolverConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * @author vssavin on 04.09.2023
 */

@Configuration
public class AppTemplateResolverConfig {

    @Bean
    public SpringResourceTemplateResolver appTemplateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setOrder(0);
        templateResolver.setCheckExistence(true);
        return templateResolver;
    }

    @Bean
    public ThymeleafViewResolver appViewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        SpringTemplateEngine templateEngine = UmTemplateResolverConfig.getSpringTemplateEngine();
		templateEngine.addDialect(new Java8TimeDialect());
        templateEngine.addTemplateResolver(appTemplateResolver());
        viewResolver.setTemplateEngine(templateEngine);
        viewResolver.setOrder(0);
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }
}
