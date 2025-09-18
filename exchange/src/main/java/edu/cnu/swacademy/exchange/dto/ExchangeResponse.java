package edu.cnu.swacademy.exchange.dto;

public class ExchangeResponse {
    String matchResult;
    int takerOrderId;
    int makerOrderId;
    int amount;

    public ExchangeResponse(String matchResult, int takerOrderId, int makerOrderId, int amount) {
        this.matchResult = matchResult;
        this.takerOrderId = takerOrderId;
        this.makerOrderId = makerOrderId;
        this.amount = amount;
    }

    public static ExchangeResponse exchangeIng(){
         return  new ExchangeResponse(OrderStatus.Matching.name(),  -1,-1,-1);
    }
    public static ExchangeResponse reject(){
        return  new ExchangeResponse(OrderStatus.Rejected.name(),  -1,-1,-1);
    }
}
