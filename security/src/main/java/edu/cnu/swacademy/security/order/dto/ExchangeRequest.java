package edu.cnu.swacademy.security.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ExchangeRequest {
    int orderId;
    int productId;
    int price;
    int amount;
    String side;
    String createdAt;

    public ExchangeRequest(int orderId, int productId, int price, int amount, String buy, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.productId = productId;
        this.price = price;
        this.amount = amount;
        this.side = buy;
        this.createdAt = createdAt.toString();
    }
}
