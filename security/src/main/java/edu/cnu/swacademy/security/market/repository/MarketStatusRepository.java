package edu.cnu.swacademy.security.market.repository;

import edu.cnu.swacademy.security.market.domain.MarketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarketStatusRepository extends JpaRepository<MarketStatus,Integer> {
    @Query("""
    SELECT m
    FROM MarketStatus m
    WHERE m.createdAt = (
        SELECT MAX(m2.createdAt)
        FROM MarketStatus m2
        WHERE m2.stock = m.stock
    )
    """)
    List<MarketStatus> findLatestMarketStatusPerStock();

    MarketStatus findTopByStockIdOrderByCreatedAtDesc(int id);
}
