package edu.cnu.swacademy.security.order.dto.orderbook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class OrderInfoResponse {
    int id;
    String created_at;
    int unfilled_quantity;
}
