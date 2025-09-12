package edu.cnu.swacademy.security.cashwallet.service;

import edu.cnu.swacademy.security.cashwallet.dto.BalanceResponse;
import edu.cnu.swacademy.security.common.SecurityException;

public interface CashWalletService {
    void createCashWallet(int userId) throws SecurityException;

    void depositWallet(int userId, int amount) throws SecurityException;

    void withdrawalWallet(int userId,  int amount) throws SecurityException;

    BalanceResponse balance(int userId) throws SecurityException;

    void cashWalletBlock(int userId) throws SecurityException;

    void cashWalletUnBlock(int userId) throws SecurityException;
}
