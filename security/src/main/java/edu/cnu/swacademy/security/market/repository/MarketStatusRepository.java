package edu.cnu.swacademy.security.market.repository;

import edu.cnu.swacademy.security.market.domain.MarketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MarketStatusRepository extends JpaRepository<MarketStatus,Integer> {
    List<MarketStatus> findAllByCreatedAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
