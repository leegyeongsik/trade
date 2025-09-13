package edu.cnu.swacademy.security.cashwallet.repository;

import edu.cnu.swacademy.security.cashwallet.domain.CashWallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashWalletRepository  extends JpaRepository<CashWallet, Integer> {

    Optional<CashWallet> findByUserId(int userId);
}
