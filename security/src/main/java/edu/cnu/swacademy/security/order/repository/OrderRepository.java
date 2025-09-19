package edu.cnu.swacademy.security.order.repository;

import edu.cnu.swacademy.security.order.domain.Order;
import edu.cnu.swacademy.security.stock.domain.Stock;
import edu.cnu.swacademy.security.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface OrderRepository extends JpaRepository<Order,Integer> {


    Page<Order> findByUserAndStockAndSide(Pageable pageable, User user, Stock stock, String side);

    Page<Order> findByIdInAndUserAndStockAndSide(Pageable pageable, Set<Integer> orderSet,User user, Stock stock, String side );
}
