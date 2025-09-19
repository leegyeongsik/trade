package edu.cnu.swacademy.security.order.dto.orderbook;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class OrderBookSell {
    List<PriceResponse> price;

}
