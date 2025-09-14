package edu.cnu.swacademy.security.order.repository;

import edu.cnu.swacademy.security.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Integer> {
}
