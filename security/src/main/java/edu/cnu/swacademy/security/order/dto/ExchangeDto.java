package edu.cnu.swacademy.security.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeDto {
    String matchResult;
    int takerOrderId;
    int makerOrderId;
    int amount;

    public ExchangeDto(String matchResult, int takerOrderId, int makerOrderId, int amount) {
        this.matchResult = matchResult;
        this.takerOrderId = takerOrderId;
        this.makerOrderId = makerOrderId;
        this.amount = amount;
    }
}
