package edu.cnu.swacademy.security.stockwallet.repository;

import edu.cnu.swacademy.security.stockwallet.domain.StockWalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockWalletHistoryRepository  extends JpaRepository<StockWalletHistory,Integer> {
}
