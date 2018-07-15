package com.vel.rest.service;

import com.vel.rest.domain.store.Customer;
import com.vel.rest.domain.store.Order;
import com.vel.rest.exception.NoRollbackException;
import com.vel.rest.exception.StoreException;

public interface StoreService {

	void store(Customer customer, Order order) throws Exception;

	String storeWithStoreException(Customer customer, Order order) throws StoreException;

	void storeWithNoRollbackException(Customer customer, Order order) throws NoRollbackException, NoRollbackException;
}
