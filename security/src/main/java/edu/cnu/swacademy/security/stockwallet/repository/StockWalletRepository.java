package edu.cnu.swacademy.security.stockwallet.repository;

import edu.cnu.swacademy.security.stockwallet.domain.StockWallet;
import edu.cnu.swacademy.security.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockWalletRepository extends JpaRepository<StockWallet,Integer> {
    Optional<StockWallet> findByUserAndStockId(User user, int stockId);
}
