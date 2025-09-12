package edu.cnu.swacademy.security.cashwallet.dto;

public record BalanceResponse(int cash_wallet_id, int savings, int tied_savings, int available) {

}
