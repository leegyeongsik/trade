package edu.cnu.swacademy.security.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeDto {
    @JsonProperty("match_result")
    String matchResult;

    @JsonProperty("taker_order_id")
    int takerOrderId;

    @JsonProperty("maker_order_id")
    int makerOrderId;
    int amount;

}
