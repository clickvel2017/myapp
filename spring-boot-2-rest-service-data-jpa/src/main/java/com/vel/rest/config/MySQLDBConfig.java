package com.vel.rest.config;

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

import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

@Configuration
@DependsOn({"transactionManager","atomikosJtaPlatform"})
@EnableJpaRepositories(basePackages = {"com.vel.rest.repository.store","com.vel.rest.repository.article","com.vel.rest.domain.store","com.vel.rest.domain.article"}, entityManagerFactoryRef = "mysqlEntityManager",
                        transactionManagerRef = "transactionManager")
public class MySQLDBConfig {
	
	@Autowired
	private Environment env;

	@Bean(name = "mysqlEntityManager")
	@DependsOn("transactionManager")
	public LocalContainerEntityManagerFactoryBean mysqlEntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		//em.setDataSource(mysqlDS());
		em.setJtaDataSource(mysqlDS());
		em.setPackagesToScan(new String[] { "com.vel.rest.repository", "com.vel.rest.domain" });
		em.setPersistenceUnitName("Mysql_PU");
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaProperties(hibernateProperties());
		return em;
	}

	@Bean(name = "mysqlDataSource", initMethod = "init", destroyMethod = "close")
	public DataSource mysqlDS() {

		 MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
		 mysqlXaDataSource.setURL(env.getProperty("mysql.datasource.jdbc-url"));
		 mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
		 mysqlXaDataSource.setPassword(env.getProperty("mysql.datasource.password"));
		 mysqlXaDataSource.setUser(env.getProperty("mysql.datasource.username"));
		 
		 
		 //MysqlConnectionPoolDataSource mysqlConnectionPoolDataSource  = new MysqlConnectionPoolDataSource ();
		
		AtomikosDataSourceBean xaDataSourceBean = new AtomikosDataSourceBean();
		xaDataSourceBean.setXaDataSource(mysqlXaDataSource);
		xaDataSourceBean.setUniqueResourceName(env.getProperty("mysql.datasource.unique-resource-name"));
		xaDataSourceBean.setMaxPoolSize(env.getProperty("mysql.datasource.max-pool-size",Integer.class));
		xaDataSourceBean.setMinPoolSize(env.getProperty("mysql.datasource.min-pool-size",Integer.class));
		xaDataSourceBean.setMaxIdleTime(env.getProperty("mysql.datasource.max-idle-time",Integer.class));
		xaDataSourceBean.setMaxLifetime(env.getProperty("mysql.datasource.max-lifetime",Integer.class));
		xaDataSourceBean.setTestQuery(env.getProperty("mysql.datasource.test-query"));
		xaDataSourceBean.setPoolSize(env.getProperty("mysql.datasource.pool-size",Integer.class));
		xaDataSourceBean.setBorrowConnectionTimeout(env.getProperty("mysql.datasource.borrow-connection-timeout",Integer.class));
		xaDataSourceBean.setReapTimeout(env.getProperty("mysql.datasource.reap-timeout",Integer.class));
		xaDataSourceBean.setMaintenanceInterval(env.getProperty("mysql.datasource.maintenance-interval",Integer.class));
		//xaDataSourceBean.setLogWriter(out);
		//xaDataSourceBean.setDefaultIsolationLevel(defaultIsolationLevel);
		return xaDataSourceBean;
	}
	
    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        //hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        hibernateProperties.setProperty("hibernate.transaction.jta.platform", "com.vel.rest.platform.AtomikosJtaPlatform");
       /* hibernateProperties.put("transaction.manager_lookup_class","com.atomikos.icatch.jta.hibernate3.TransactionManagerLookup");
        hibernateProperties.put("transaction.factory_class","com.atomikos.icatch.jta.hibernate3.AtomikosJTATransactionFactory");*/
        hibernateProperties.put("hibernate.current_session_context_class", "jta");
        //hibernateProperties.put("javax.persistence.transactionType", "JTA");
       

        return hibernateProperties;
    }

}
