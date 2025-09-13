package edu.cnu.swacademy.security.cashwallet.repository;

import edu.cnu.swacademy.security.cashwallet.domain.CashWallet;
import edu.cnu.swacademy.security.cashwallet.domain.CashWalletHistory;
import edu.cnu.swacademy.security.cashwallet.dto.CashWalletHistoriesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashWalletHistoryRepository extends JpaRepository<CashWalletHistory, Integer> {
    Page<CashWalletHistory> findByCashWallet(Pageable pageable, CashWallet cashWallet);
}
