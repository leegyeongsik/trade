package edu.cnu.swacademy.exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExchangeResponse {
    @JsonProperty("match_result")
    String matchResult;

    @JsonProperty("taker_order_id")
    int takerOrderId;

    @JsonProperty("maker_order_id")
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
