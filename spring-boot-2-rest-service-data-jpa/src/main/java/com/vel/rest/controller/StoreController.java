package com.vel.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vel.rest.domain.store.Customer;
import com.vel.rest.domain.store.Order;
import com.vel.rest.exception.NoRollbackException;
import com.vel.rest.exception.StoreException;
import com.vel.rest.service.StoreService;

@RestController
@RequestMapping("CustomerAndOrders")
public class StoreController {

	@Autowired
	public StoreService storeService;

	@RequestMapping("store")
	public ResponseEntity<String> store(Customer customer, Order order) throws Exception {

		return new ResponseEntity<String>(HttpStatus.OK);

	}

	public ResponseEntity<String> storeWithStoreException(Customer customer, Order order) throws StoreException {
		storeService.storeWithStoreException(customer, order);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	public ResponseEntity<String> storeWithNoRollbackException(Customer customer, Order order)
			throws NoRollbackException, NoRollbackException, StoreException {
		storeService.storeWithStoreException(customer, order);
		return new ResponseEntity<String>(HttpStatus.OK);
	}

}
