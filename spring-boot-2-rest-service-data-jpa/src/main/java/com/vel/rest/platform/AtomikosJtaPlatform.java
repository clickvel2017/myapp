package com.vel.rest.platform;

import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.hibernate.engine.transaction.jta.platform.internal.AbstractJtaPlatform;
import org.springframework.stereotype.Component;

@Component("atomikosJtaPlatform")
public class AtomikosJtaPlatform extends AbstractJtaPlatform {

	private static final long serialVersionUID = -8944248310415653529L;
	private static TransactionManager transactionManager;
	private static UserTransaction transaction;
	
	@Override
	protected TransactionManager locateTransactionManager() {
		// TODO Auto-generated method stub
		return transactionManager;
	}

	@Override
	protected UserTransaction locateUserTransaction() {
		// TODO Auto-generated method stub
		return transaction;
	}

	public void setTransaction(UserTransaction transaction) {
		AtomikosJtaPlatform.transaction = transaction;
	}
	
	public void setTransactionManager(TransactionManager transactionManager) {
		AtomikosJtaPlatform.transactionManager = transactionManager;
	}
}
