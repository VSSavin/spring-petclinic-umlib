package org.springframework.samples.petclinic;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * @author vssavin on 11.09.2023
 */
@Configuration
@EnableAutoConfiguration(exclude = {ThymeleafAutoConfiguration.class})
public class ConfigForTesting {
}
