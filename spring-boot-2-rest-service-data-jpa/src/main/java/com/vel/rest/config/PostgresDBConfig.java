package com.vel.rest.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.vel.rest.platform.AtomikosJtaPlatform;

@Configuration
@DependsOn("transactionManager")
@EnableJpaRepositories(basePackages = { "com.vel.rest.repository.exam",
		"com.vel.rest.domain.exam" }, entityManagerFactoryRef = "postgresEntityManager", transactionManagerRef = "transactionManager")
public class PostgresDBConfig {

	@Autowired
	private Environment env;

	@Bean(name = "postgresEntityManager")
	@DependsOn("transactionManager")
	public LocalContainerEntityManagerFactoryBean postgresEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(postgresDS());
		em.setPackagesToScan(new String[] { "com.vel.rest.repository", "com.vel.rest.domain" });
		em.setPersistenceUnitName("Postgres_PU");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaProperties(hibernateProperties());
		return em;
	}

	@Bean(name = "postgresDataSource", initMethod = "init", destroyMethod = "close")
	public DataSource postgresDS() {

		PGXADataSource pGXADataSource = new PGXADataSource();
		pGXADataSource.setURL(env.getProperty("postgresql.datasource.jdbc-url"));
		pGXADataSource.setUser(env.getProperty("postgresql.datasource.username"));
		pGXADataSource.setPassword(env.getProperty("postgresql.datasource.password"));

		AtomikosDataSourceBean xaDataSourceBean = new AtomikosDataSourceBean();
		xaDataSourceBean.setXaDataSource(pGXADataSource);
		xaDataSourceBean.setUniqueResourceName(env.getProperty("postgresql.datasource.unique-resource-name"));
		xaDataSourceBean.setMaxPoolSize(env.getProperty("postgresql.datasource.max-pool-size", Integer.class));
		xaDataSourceBean.setMinPoolSize(env.getProperty("postgresql.datasource.min-pool-size", Integer.class));
		xaDataSourceBean.setMaxIdleTime(env.getProperty("postgresql.datasource.max-idle-time", Integer.class));
		xaDataSourceBean.setMaxLifetime(env.getProperty("postgresql.datasource.max-lifetime", Integer.class));
		xaDataSourceBean.setTestQuery(env.getProperty("postgresql.datasource.test-query"));
		xaDataSourceBean.setPoolSize(env.getProperty("postgresql.datasource.pool-size", Integer.class));
		xaDataSourceBean.setBorrowConnectionTimeout(
				env.getProperty("postgresql.datasource.borrow-connection-timeout", Integer.class));
		xaDataSourceBean.setReapTimeout(env.getProperty("postgresql.datasource.reap-timeout", Integer.class));
		xaDataSourceBean
				.setMaintenanceInterval(env.getProperty("postgresql.datasource.maintenance-interval", Integer.class));
		// xaDataSourceBean.setLogWriter(out);
		// xaDataSourceBean.setDefaultIsolationLevel(defaultIsolationLevel);

		return xaDataSourceBean;
	}

	private Properties hibernateProperties() {
		final Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		hibernateProperties.setProperty("hibernate.jdbc.lob.non_contextual_creation", "true");
		// hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
		hibernateProperties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
		hibernateProperties.put("hibernate.current_session_context_class", "jta");
		// hibernateProperties.put("javax.persistence.transactionType", "JTA");

		return hibernateProperties;
	}

}
