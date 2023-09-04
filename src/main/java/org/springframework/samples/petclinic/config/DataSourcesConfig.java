package org.springframework.samples.petclinic.config;

import com.github.vssavin.umlib.config.UmDataSourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * @author vssavin on 04.09.2023
 */
@Configuration
@Import(UmDataSourceConfig.class)
public class DataSourcesConfig {
	private static final Logger log = LoggerFactory.getLogger(DataSourcesConfig.class);
	private final DatabaseConfig databaseConfig;
    private DataSource appDataSource;

	@Autowired
	public DataSourcesConfig(DatabaseConfig databaseConfig) {
		this.databaseConfig = databaseConfig;
	}

    @Bean
    public DataSource appDataSource() {
		if (this.appDataSource != null) {
			return this.appDataSource;
		}
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		try {
			dataSource.setDriverClassName(databaseConfig.getDriverClass());
			String url = databaseConfig.getUrl() + "/"
				+ databaseConfig.getName();
			if (databaseConfig.getDriverClass().equals("org.h2.Driver")) {
				url += ";" + databaseConfig.getAdditionalParams();
			}
			dataSource.setUrl(url);
			dataSource.setUsername(databaseConfig.getUser());
			dataSource.setPassword(databaseConfig.getPassword());
		} catch (Exception e) {
			log.error("Creating datasource error: ", e);
		}
		this.appDataSource = dataSource;

		return dataSource;
    }

	@Bean
	public DatabasePopulator resourceDatabasePopulator(DataSource appDataSource) {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScripts(
			new ClassPathResource("db/h2/schema.sql"),
			new ClassPathResource("db/h2/data.sql"));
		populator.setSeparator("@@");
		populator.execute(appDataSource);
		return populator;
	}
}
