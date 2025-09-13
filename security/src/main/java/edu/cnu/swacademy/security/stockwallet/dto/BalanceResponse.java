package edu.cnu.swacademy.security.stockwallet.dto;

public record BalanceResponse(int id, int stock_id, int savings, int tied_savings, int available) {

}
