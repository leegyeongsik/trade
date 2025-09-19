package edu.cnu.swacademy.security.order.service;

import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.order.dto.*;
import edu.cnu.swacademy.security.order.dto.orderbook.OrderBookResponse;

public interface OrderService {
    ExchangeDto order(int userId, OrderRequest orderRequest) throws SecurityException;

    void orderResult(ExchangeDto exchangeDto) throws SecurityException;

    void orderCancel(int userId, int orderId) throws SecurityException;

    OrderUnfilledsResponse getUnfilledOrders(int userId, int stockId, String side, int page, int size, String sort) throws SecurityException;

    OrderFilledsResponse getMatchs(int userId, int stockId, String side, int page, int size, String sort) throws SecurityException;

    OrderBookResponse getOrderBook(int userId, int stockId) throws SecurityException;
}
