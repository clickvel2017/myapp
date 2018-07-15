package com.vel.rest.config;

import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

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

import oracle.jdbc.xa.client.OracleXADataSource;

@Configuration
@DependsOn("transactionManager")
@EnableJpaRepositories(basePackages =  {"com.vel.rest.repository.player","com.vel.rest.repository.book", "com.vel.rest.domain.player","com.vel.rest.domain.book"}, 
                                        entityManagerFactoryRef = "oracleEntityManager",transactionManagerRef = "transactionManager")
public class OracleDBConfig {

	@Autowired
	private Environment env;

	@Bean(name = "oracleEntityManager")
	@DependsOn("transactionManager")
	public LocalContainerEntityManagerFactoryBean oracleEntityManager() throws SQLException {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(oracleDS());
		em.setPackagesToScan(new String[] {"com.vel.rest.repository", "com.vel.rest.domain"});
		em.setPersistenceUnitName("Oracle_PU");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaProperties(hibernateProperties());
		return em;
	}

	@Bean(name = "oracleDataSource", initMethod = "init", destroyMethod = "close")
	public DataSource oracleDS() throws SQLException {
		/*PoolProperties p = new PoolProperties();
		p.setUrl(env.getProperty("oracle.datasource.jdbc-url"));
		p.setDriverClassName(env.getProperty("oracle.datasource.driver-class-name"));
		p.setUsername(env.getProperty("oracle.datasource.username"));
		p.setPassword(env.getProperty("oracle.datasource.password"));
		p.setJmxEnabled(true);
		p.setTestWhileIdle(env.getProperty("oracle.datasource.test-while-idle",Boolean.class));
		p.setTestOnBorrow(env.getProperty("oracle.datasource.test-on-borrow",Boolean.class));
		p.setValidationQuery(env.getProperty("oracle.datasource.connection-test-query"));
		p.setTestOnReturn(false);
		p.setValidationInterval(30000);
		p.setTimeBetweenEvictionRunsMillis(30000);
		p.setMaxActive(env.getProperty("oracle.datasource.max-active",Integer.class));
		p.setInitialSize(env.getProperty("oracle.datasource.initial-size",Integer.class));
		p.setMaxWait(env.getProperty("oracle.datasource.max-wait",Integer.class));
		p.setRemoveAbandonedTimeout(60);
		p.setMinEvictableIdleTimeMillis(30000);
		p.setMaxIdle(env.getProperty("oracle.datasource.max-idle",Integer.class));
		p.setMinIdle(env.getProperty("oracle.datasource.min-idle",Integer.class));
		p.setLogAbandoned(true);
		p.setRemoveAbandoned(true);
		p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
				+ "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");

		XADataSource xaDatasource = new XADataSource();
		xaDatasource.setPoolProperties(p);*/
		
		/*DataSource datasource = new DataSource();
		datasource.setPoolProperties(p);
		DataSource dataSource = new DataSource(); 
        dataSource.setDriverClassName(driverClassName); dataSource.setUrl(url);
		dataSource.setUsername(username); dataSource.setPassword(password);
		dataSource.setTestWhileIdle(testWhileIdle);
		dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMills);
		dataSource.setValidationQuery(validationQuery);*/
		

		OracleXADataSource oracleXADataSource = new OracleXADataSource();
		oracleXADataSource.setURL(env.getProperty("oracle.datasource.jdbc-url"));
		oracleXADataSource.setPassword(env.getProperty("oracle.datasource.password"));
		oracleXADataSource.setUser(env.getProperty("oracle.datasource.username"));	
		
		AtomikosDataSourceBean xaDataSourceBean = new AtomikosDataSourceBean();
		xaDataSourceBean.setXaDataSource(oracleXADataSource);
		xaDataSourceBean.setUniqueResourceName(env.getProperty("oracle.datasource.unique-resource-name"));
		xaDataSourceBean.setMaxPoolSize(env.getProperty("oracle.datasource.max-pool-size",Integer.class));
		xaDataSourceBean.setMinPoolSize(env.getProperty("oracle.datasource.min-pool-size",Integer.class));
		xaDataSourceBean.setMaxIdleTime(env.getProperty("oracle.datasource.max-idle-time",Integer.class));
		xaDataSourceBean.setMaxLifetime(env.getProperty("oracle.datasource.max-lifetime",Integer.class));
		xaDataSourceBean.setTestQuery(env.getProperty("oracle.datasource.test-query"));
		xaDataSourceBean.setPoolSize(env.getProperty("oracle.datasource.pool-size",Integer.class));
		xaDataSourceBean.setBorrowConnectionTimeout(env.getProperty("oracle.datasource.borrow-connection-timeout",Integer.class));
		xaDataSourceBean.setReapTimeout(env.getProperty("oracle.datasource.reap-timeout",Integer.class));
		xaDataSourceBean.setMaintenanceInterval(env.getProperty("oracle.datasource.maintenance-interval",Integer.class));
		//xaDataSourceBean.setLogWriter(out);
		//xaDataSourceBean.setDefaultIsolationLevel(defaultIsolationLevel);
		return xaDataSourceBean;
	}
	
    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.OracleDialect");
       // hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        hibernateProperties.put("hibernate.current_session_context_class", "jta");
        //hibernateProperties.put("javax.persistence.transactionType", "JTA");

        return hibernateProperties;
    }

}
