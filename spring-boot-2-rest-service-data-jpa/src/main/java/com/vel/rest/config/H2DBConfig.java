package com.vel.rest.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.apache.tomcat.jdbc.pool.XADataSource;
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

//@Configuration
@DependsOn("transactionManager")
@EnableJpaRepositories(basePackages = {"com.vel.rest.repository", "com.vel.rest.domain"}, entityManagerFactoryRef = "h2EntityManager",
                        transactionManagerRef = "transactionManager")
public class H2DBConfig {

	@Autowired
	private Environment env;

	@Bean(name = "h2EntityManager")
	@DependsOn("transactionManager")
	public LocalContainerEntityManagerFactoryBean h2EntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(h2DS());
		em.setPackagesToScan(new String[] {"com.vel.rest.repository", "com.vel.rest.domain"});
		em.setPersistenceUnitName("H2_PU");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaProperties(hibernateProperties());
		return em;
	}

	@Bean(name = "h2DataSource", initMethod = "init", destroyMethod = "close")
	public DataSource h2DS() {
		PoolProperties p = new PoolProperties();
		p.setUrl(env.getProperty("h2.datasource.jdbc-url"));
		p.setDriverClassName(env.getProperty("h2.datasource.driver-class-name"));
		p.setUsername(env.getProperty("h2.datasource.username"));
		p.setPassword(env.getProperty("h2.datasource.password"));
		p.setJmxEnabled(true);
		p.setTestWhileIdle(env.getProperty("h2.datasource.test-while-idle",Boolean.class));
		p.setTestOnBorrow(env.getProperty("h2.datasource.test-on-borrow",Boolean.class));
		p.setValidationQuery(env.getProperty("h2.datasource.connection-test-query"));
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(env.getProperty("h2.datasource.max-active",Integer.class));
		p.setInitialSize(env.getProperty("h2.datasource.initial-size",Integer.class));
		p.setMaxWait(env.getProperty("h2.datasource.max-wait",Integer.class));
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMaxIdle(env.getProperty("h2.datasource.max-idle",Integer.class));
		p.setMinIdle(env.getProperty("h2.datasource.min-idle",Integer.class));
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");

		XADataSource xaDatasource = new XADataSource();
		xaDatasource.setPoolProperties(p);

		/*DataSource datasource = new DataSource();
		datasource.setPoolProperties(p);
		DataSource dataSource = new DataSource(); 
        dataSource.setDriverClassName(driverClassName); dataSource.setUrl(url);
		dataSource.setUsername(username); dataSource.setPassword(password);
		dataSource.setTestWhileIdle(testWhileIdle);
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMills);
		dataSource.setValidationQuery(validationQuery);*/
		
		AtomikosDataSourceBean xaDataSourceBean = new AtomikosDataSourceBean();
		xaDataSourceBean.setXaDataSource(xaDatasource);
		xaDataSourceBean.setUniqueResourceName("h2XADS");
		return xaDataSourceBean;
	}
	
    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        hibernateProperties.put("javax.persistence.transactionType", "JTA");

        return hibernateProperties;
    }

}
