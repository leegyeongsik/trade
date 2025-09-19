package edu.cnu.swacademy.security.order.dto.orderbook;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OrderBookResponse {
    private OrderBookBuy buy;
    private OrderBookSell sell;
}

