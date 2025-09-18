package edu.cnu.swacademy.security.order.service;

import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.market.dto.MarketResponse;
import edu.cnu.swacademy.security.order.dto.ExchangeResponse;
import edu.cnu.swacademy.security.order.dto.OrderRequest;

public interface OrderService {
    ExchangeResponse order(int userId, OrderRequest orderRequest) throws SecurityException;
}
