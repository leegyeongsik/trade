package edu.cnu.swacademy.security.market.repository;

import edu.cnu.swacademy.security.market.domain.MarketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MarketStatusRepository extends JpaRepository<MarketStatus,Integer> {

    Optional<MarketStatus> findFirstByOrderByCreatedAtDesc();

    List<MarketStatus> findAllByCreatedAt(LocalDateTime createdAt);
}
