package edu.cnu.swacademy.security.stockwallet.service;

import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.stockwallet.dto.BalanceResponse;

public interface StockService {
    void createStockWallet(int userId, int stockId) throws SecurityException;

    void depositWallet(int stockWalletId, int amount) throws SecurityException;

    BalanceResponse balance(int userId, int stockId) throws SecurityException;

    void WalletBlock(int stockWalletId) throws SecurityException;

    void WalletUnBlock(int stockWalletId) throws SecurityException;
}
