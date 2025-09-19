package edu.cnu.swacademy.security.order.dto;

import edu.cnu.swacademy.security.order.domain.Order;

public class OrderUnfilledResponse {

    int stock_id;
    int order_id;
    String side;
    int price;
    int quantity;
    int unfilled_quantity;
    int canceled_quantity;
    String created_at;
    public OrderUnfilledResponse(Order order){
        this.stock_id = order.getStock().getId();
        this.order_id = order.getId();
        this.side = order.getSide().name();
        this.price = order.getPrice();
        this.quantity = order.getAmount();
        this.unfilled_quantity = order.getUnfilledAmount();
        this.canceled_quantity = order.getCanceledAmount();
        this.created_at = String.valueOf(order.getCreatedAt());
    }
}
