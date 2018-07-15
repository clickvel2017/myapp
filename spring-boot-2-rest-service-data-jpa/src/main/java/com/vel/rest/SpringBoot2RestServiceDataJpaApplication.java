package com.vel.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class,DataSourceTransactionManagerAutoConfiguration.class,
		                         XADataSourceAutoConfiguration.class})
@EnableJpaAuditing
public class SpringBoot2RestServiceDataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBoot2RestServiceDataJpaApplication.class, args);
	}
}
