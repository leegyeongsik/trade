package edu.cnu.swacademy.security.order.dto.orderbook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Setter
@Getter
public class OrderInfoResponse {
    @JsonProperty("order_id")
    private int id;

    @JsonProperty("create_date")
    private LocalDateTime createdAt;

    @JsonProperty("unfilled_unit")
    private int unfilledQuantity;
}
