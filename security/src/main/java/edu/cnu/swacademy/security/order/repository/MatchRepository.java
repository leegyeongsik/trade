package edu.cnu.swacademy.security.order.repository;

import edu.cnu.swacademy.security.order.domain.Match;
import edu.cnu.swacademy.security.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match,Integer> {
    List<Match> findByStock(Stock stock);
}
