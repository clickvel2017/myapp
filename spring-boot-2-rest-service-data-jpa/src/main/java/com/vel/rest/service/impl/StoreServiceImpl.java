package com.vel.rest.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vel.rest.domain.store.Customer;
import com.vel.rest.domain.store.Order;
import com.vel.rest.exception.NoRollbackException;
import com.vel.rest.exception.StoreException;
import com.vel.rest.repository.store.CustomerRepository;
import com.vel.rest.repository.store.OrderRepository;
import com.vel.rest.service.StoreService;

@Service
public class StoreServiceImpl implements StoreService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private OrderRepository orderRepository;

	@Override
	@Transactional
	public void store(Customer customer, Order order) {
		customerRepository.save(customer);
		orderRepository.save(order);
	}

	@Transactional(rollbackOn = StoreException.class)
	@Override
	public String storeWithStoreException(Customer customer, Order order) throws StoreException {
		customerRepository.save(customer);
		orderRepository.save(order);
		throw new StoreException();
	}

	@Transactional(dontRollbackOn = NoRollbackException.class, rollbackOn = StoreException.class)
	@Override
	public void storeWithNoRollbackException(Customer customer, Order order) throws NoRollbackException {
		customerRepository.save(customer);
		orderRepository.save(order);
		throw new NoRollbackException();
	}
}
