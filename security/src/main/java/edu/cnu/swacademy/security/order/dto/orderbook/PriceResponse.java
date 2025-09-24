package edu.cnu.swacademy.security.order.dto.orderbook;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class PriceResponse {
    String price;
    TotalQuantityResponse total_quantity;
    List<OrderInfoResponse> orders;
}
