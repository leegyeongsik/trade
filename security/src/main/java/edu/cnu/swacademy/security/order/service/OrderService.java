package edu.cnu.swacademy.security.order.service;

import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.order.dto.ExchangeDto;
import edu.cnu.swacademy.security.order.dto.OrderRequest;

public interface OrderService {
    ExchangeDto order(int userId, OrderRequest orderRequest) throws SecurityException;

    void orderResult(ExchangeDto exchangeDto) throws SecurityException;

    void orderCancel(int userId, int orderId);
}
