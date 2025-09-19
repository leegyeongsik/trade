package edu.cnu.swacademy.security.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExchangeCancelRequest {
    int orderId;
    int stockId;
    public ExchangeCancelRequest(int orderId , int stockId){
        this.orderId = orderId;
        this.stockId = stockId;
    }
}
