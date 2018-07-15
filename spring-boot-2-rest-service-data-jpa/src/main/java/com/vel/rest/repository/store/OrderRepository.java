package com.vel.rest.repository.store;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vel.rest.domain.store.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

}
