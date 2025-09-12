package edu.cnu.swacademy.security.cashwallet.dto;

import java.util.List;

public record CashWalletHistoriesResponse(int total_elements, List<CashWalletHistoryResponse> rows) {
}
