package edu.cnu.swacademy.security.order.dto;

import edu.cnu.swacademy.security.cashwallet.dto.CashWalletHistoryResponse;

import java.util.List;

public record OrderUnfilledsResponse(int total_elements, List<OrderUnfilledResponse> rows) {

}
