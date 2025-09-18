package edu.cnu.swacademy.exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RedisOrderDto {

    @JsonProperty("order_id")
    private int orderId;

    @JsonProperty("create_date")
    private String createDate;

    @JsonProperty("unfilled_unit")
    private int unfilledUnit;

    public RedisOrderDto(int orderId, String createDate, int unfilledUnit) {
        this.orderId = orderId;
        this.createDate = createDate;
        this.unfilledUnit = unfilledUnit;
    }

}