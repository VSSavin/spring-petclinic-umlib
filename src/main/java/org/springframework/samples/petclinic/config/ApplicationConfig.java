package org.springframework.samples.petclinic.config;

import com.github.vssavin.umlib.config.AuthorizedUrlPermission;
import com.github.vssavin.umlib.config.DefaultSecurityConfig;
import com.github.vssavin.umlib.config.Permission;
import com.github.vssavin.umlib.config.UmConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.*;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author vssavin on 04.09.2023
 */
@Configuration
@ComponentScan({"com.github.vssavin.umlib", "org.springframework.samples.petclinic"})
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.github.vssavin.umlib.domain", "org.springframework.samples.petclinic"})
@EnableWebSecurity
@Import(DefaultSecurityConfig.class)
public class ApplicationConfig {
	private static final Logger log = LoggerFactory.getLogger(ApplicationConfig.class);

	@Bean
	public UmConfigurer umConfigurer() {
		Map<String, String[]> resourceHandlers = new HashMap<>();
		resourceHandlers.put("/resources/css/**", new String[]{"classpath:/static/resources/css/"});
		resourceHandlers.put("/resources/fonts/**", new String[]{"classpath:/static/resources/fonts/"});
		resourceHandlers.put("/resources/images/**", new String[]{"classpath:/static/resources/images/"});

		return new UmConfigurer().successUrl("/index.html")
			.permission(new AuthorizedUrlPermission("/**", Permission.USER_ADMIN))
			.permission(new AuthorizedUrlPermission("/index.html", Permission.ANY_USER))
			.csrf(false)
			.resourceHandlers(resourceHandlers)
			.configure();
	}

	@Bean
	public FilterRegistrationBean<HiddenHttpMethodFilter> hiddenHttpMethodFilter() {
		FilterRegistrationBean<HiddenHttpMethodFilter> filterBean =
			new FilterRegistrationBean<>(new HiddenHttpMethodFilter());
		filterBean.setUrlPatterns(Collections.singletonList("/*"));
		return filterBean;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource routingDataSource,
																	   DatabaseConfig databaseConfig) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

		try {
			em.setDataSource(routingDataSource);
			em.setPackagesToScan("com.github.vssavin.umlib.domain", "org.springframework.samples.petclinic");

			em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
			String hibernateDialect = databaseConfig.getDialect();

			Properties additionalProperties = new Properties();
			additionalProperties.put("hibernate.dialect", hibernateDialect);
			em.setJpaProperties(additionalProperties);
		} catch (Exception e) {
			log.error("Creating LocalContainerEntityManagerFactoryBean error!", e);
		}

		return em;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
		return new JpaTransactionManager(emf);
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}
}
