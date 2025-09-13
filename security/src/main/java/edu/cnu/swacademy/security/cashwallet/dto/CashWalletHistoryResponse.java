package edu.cnu.swacademy.security.cashwallet.dto;

import edu.cnu.swacademy.security.cashwallet.domain.CashWalletHistory;
import lombok.Getter;

@Getter
public class CashWalletHistoryResponse{
    int history_id;
    String category;
    long amount;
    String reason;
    long savings;
    String create_at;
    public CashWalletHistoryResponse(CashWalletHistory cashWalletHistory){
        this.history_id = cashWalletHistory.getId();
        this.category = cashWalletHistory.getTxType();
        this.amount = cashWalletHistory.getTxAmount();
        this.reason = cashWalletHistory.getTxNote();
        this.savings = cashWalletHistory.getReserve();
        this.create_at = String.valueOf(cashWalletHistory.getCreatedAt());

    }

}
