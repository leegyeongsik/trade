package edu.cnu.swacademy.exchange.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDeleteRequest {
    @Min(1)
    int orderId;
    @Min(1)
    int stockId;

}
