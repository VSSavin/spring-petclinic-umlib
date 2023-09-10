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

		initDatabase(appDataSource);
		return dataSource;
    }

	private void initDatabase(DataSource appDataSource) {
		ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		String databaseType = "";
		switch (databaseConfig.getDriverClass()) {
			case "org.h2.Driver":
				databaseType = "h2";
				break;
			case "org.postgresql.Driver":
				databaseType = "postgres";
				break;
			case "org.hsqldb.jdbc.JDBCDriver":
				databaseType = "hsqldb";
				break;
			case "com.mysql.jdbc.Driver":
				databaseType = "mysql";
				break;
			default:
		}
		populator.addScripts(
			new ClassPathResource(String.format("db/%s/schema.sql", databaseType)),
			new ClassPathResource(String.format("db/%s/data.sql", databaseType)));
		populator.execute(appDataSource);
	}
}
