package edu.cnu.swacademy.security.order.dto;

public class ExchangeResponse {
    String matchResult;
    int takerOrderId;
    int makerOrderId;
    String takerSide;
    String makerSide;
    int amount;

    public ExchangeResponse(String matchResult, int takerOrderId, int makerOrderId, String takerSide , String makerSide, int amount) {
        this.matchResult = matchResult;
        this.takerOrderId = takerOrderId;
        this.makerOrderId = makerOrderId;
        this.takerSide = takerSide;
        this.makerSide = makerSide;
        this.amount = amount;
    }
}
