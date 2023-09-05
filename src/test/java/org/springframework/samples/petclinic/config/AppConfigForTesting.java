package org.springframework.samples.petclinic.config;

import com.github.vssavin.umlib.config.AuthorizedUrlPermission;
import com.github.vssavin.umlib.config.Permission;
import com.github.vssavin.umlib.config.UmConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.HashMap;
import java.util.Map;


/**
 * @author vssavin on 05.09.2023
 */
@Configuration
@Import({ApplicationConfig.class})
public class AppConfigForTesting {

	@Bean
	@Primary
	public UmConfigurer umConfigurerBean() {
		Map<String, String[]> resourceHandlers = new HashMap<>();
		resourceHandlers.put("/resources/css/**", new String[]{"classpath:/static/resources/css/"});
		resourceHandlers.put("/resources/fonts/**", new String[]{"classpath:/static/resources/fonts/"});
		resourceHandlers.put("/resources/images/**", new String[]{"classpath:/static/resources/images/"});

		return new UmConfigurer().successUrl("/index.html")
			.permission(new AuthorizedUrlPermission("/owner", Permission.ANY_USER))
			.permission(new AuthorizedUrlPermission("/owner/*", Permission.ANY_USER))
			.permission(new AuthorizedUrlPermission("/owners", Permission.ANY_USER))
			.permission(new AuthorizedUrlPermission("/owners/*", Permission.ANY_USER))
			.permission(new AuthorizedUrlPermission("/vets", Permission.ANY_USER))
			.permission(new AuthorizedUrlPermission("/vets/*", Permission.ANY_USER))
			.resourceHandlers(resourceHandlers)
			.csrf(false)
			.configure();
	}
}
