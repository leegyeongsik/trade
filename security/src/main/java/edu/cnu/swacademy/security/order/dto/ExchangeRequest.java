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
    String create_at;

    public ExchangeRequest(int orderId, int productId, int price, int amount, String buy, LocalDateTime createdAt) {
    }
}
