package com.vel.rest.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vel.rest.domain.store.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
