package edu.cnu.swacademy.security.common;

import edu.cnu.swacademy.security.auth.domain.Authentication;
import edu.cnu.swacademy.security.cashwallet.domain.CashWallet;
import edu.cnu.swacademy.security.stock.domain.Stock;
import edu.cnu.swacademy.security.stockwallet.domain.StockWallet;
import edu.cnu.swacademy.security.user.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class Validate {

    public void passwordValidation(String a, String b) throws SecurityException {
        if (!a.equals(b)) {
            throw new SecurityException(ErrorCode.INVALID_CREDENTIALS);
        }
    }

    public User isUser(Optional<User> user) throws SecurityException {
        return user.orElseThrow(() ->
                new SecurityException(ErrorCode.USER_NOT_FOUND));
    }

    public Stock isStock(Optional<Stock> OptionalStock) throws SecurityException {
        return OptionalStock.orElseThrow(() ->
                new SecurityException(ErrorCode.STOCK_NOT_FOUND));
    }

    public CashWallet isCashWallet(Optional<CashWallet> OptionalCashWallet) throws SecurityException {
        return OptionalCashWallet.orElseThrow(() ->
                new SecurityException(ErrorCode.CASH_WALLET_NOT_FOUND));
    }

    public Authentication isRefreshTokenAuthentication(Optional<Authentication> authentication) throws SecurityException {
        return authentication.orElseThrow(() ->
                new SecurityException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));

    }

    public StockWallet isStockWallet(Optional<StockWallet> OptionalStockWallet) throws SecurityException {
        return OptionalStockWallet.orElseThrow(() ->
                new SecurityException(ErrorCode.STOCK_NOT_FOUND));
    }

    public void authenticationValidation(Authentication authenticationToken) throws SecurityException {
        if (authenticationToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new SecurityException(ErrorCode.UNAUTHORIZED);
        }
    }

    public Authentication checkAuthentication(Optional<Authentication> authentication) {
        return authentication.get();
    }

    public void existsByEmail(boolean b) throws SecurityException {
        if (b) {
            throw new SecurityException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
    }

    public void checkWithdrawal(long currentPossibleWithdrawal, int amount) throws SecurityException {
        if (amount > currentPossibleWithdrawal) {
            throw new SecurityException(ErrorCode.INSUFFICIENT_BALANCE);
        }
    }

    public void isBlock(boolean blocked, boolean isCashWallet) throws SecurityException {
        if (isCashWallet) {
            if (blocked) {
                throw new SecurityException(ErrorCode.CASH_WALLET_BLOCKED);
            }
        } else {
            if (blocked) {
                throw new SecurityException(ErrorCode.STOCK_WALLET_BLOCKED);
            }
        }
    }

    public void unBlock(boolean blocked, boolean isCashWallet) throws SecurityException {
        if (isCashWallet) {
            if (!blocked) {
                throw new SecurityException(ErrorCode.CASH_WALLET_ALREADY_UNBLOCKED);
            }
            throw new SecurityException(ErrorCode.CASH_WALLET_ALREADY_UNBLOCKED);
        } else {
            if (!blocked) {
                throw new SecurityException(ErrorCode.STOCK_WALLET_ALREADY_UNBLOCKED);
            }
        }
    }
    public void exchangeServerNotExistCheck(boolean alive) throws SecurityException {
        if(alive){
            throw new SecurityException(ErrorCode.MARKET_ALREADY_OPEN);
        }
    }


    public void exchangeServerExistCheck(boolean alive) throws SecurityException {
        if(!alive){
            throw new SecurityException(ErrorCode.MARKET_ALREADY_CLOSED);
        }
    }
}
