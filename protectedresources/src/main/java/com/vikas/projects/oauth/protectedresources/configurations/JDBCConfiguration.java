package com.vikas.projects.oauth.protectedresources.configurations;

import java.sql.Driver;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

@Configuration
public class JDBCConfiguration {

	@Value("${driverClassName}")
	private String driverClassName;
	@Value("${url}")
	private String url;
	@Value("${username}")
	private String username;
	@Value("${password}")
	private String password;

	@Lazy
	@Bean
	public DataSource getDataSource() throws Exception {
		try {
			SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
			Class<? extends Driver> driver = (Class<? extends Driver>) Class.forName(driverClassName);
			dataSource.setDriverClass(driver);
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			return dataSource;
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Lazy
	@Bean
	public JdbcTemplate getJdbcTemplate() {
		try {
			JdbcTemplate jdbcTemplate = new JdbcTemplate();
			jdbcTemplate.setDataSource(getDataSource());
			return jdbcTemplate;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Lazy
	@Bean
	public NamedParameterJdbcTemplate getNamedJdbcTemplate() {
		try {
			NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(getDataSource());
			return jdbcTemplate;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
