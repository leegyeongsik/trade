package edu.cnu.swacademy.security.stock.repository;

import edu.cnu.swacademy.security.stock.domain.StockWalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockWalletHistoryRepository  extends JpaRepository<StockWalletHistory,Integer> {
}
