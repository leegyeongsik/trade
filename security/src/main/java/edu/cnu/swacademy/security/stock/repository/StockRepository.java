package edu.cnu.swacademy.security.stock.repository;

import edu.cnu.swacademy.security.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock,Integer> {
}
