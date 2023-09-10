package org.springframework.samples.petclinic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author vssavin on 04.09.2023
 */

@Component
@ConfigurationProperties(prefix = DatabaseConfig.NAME_PREFIX)
@PropertySource("file:./" + DatabaseConfig.CONFIG_FILE)
public class DatabaseConfig {
    public static final String CONFIG_FILE = "conf.properties";

	public static final String NAME_PREFIX = "db";

    private String url;
    private String driverClass;
    private String dialect;
    private String name;
    private String user;
    private String password;
	private String additionalParams;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getName() {
        return name;
    }

	public String getAdditionalParams() {
		return additionalParams;
	}

	public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public void setAdditionalParams(String additionalParams) {
		this.additionalParams = additionalParams;
	}
}
