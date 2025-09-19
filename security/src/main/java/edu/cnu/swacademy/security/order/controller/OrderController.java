package edu.cnu.swacademy.security.order.controller;

import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.order.dto.*;
import edu.cnu.swacademy.security.order.dto.orderbook.OrderBookResponse;
import edu.cnu.swacademy.security.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController("/api/v1/")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping()
    void order(HttpServletRequest request , @Valid @RequestBody OrderRequest orderRequest) throws SecurityException, IOException {
        int userId = (int) request.getAttribute("user_id");
        orderService.order(userId, orderRequest);
    }

    @PostMapping("order/result")
    void result(@RequestBody ExchangeDto exchangeDto) throws SecurityException {
        orderService.orderResult(exchangeDto);
    }

    @GetMapping("order/{orderId}")
    void orderCancel(HttpServletRequest request, @PathVariable int orderId ) throws SecurityException {
        int userId = (int) request.getAttribute("user_id");
        orderService.orderCancel(userId,orderId);
    }

    @GetMapping("order/unfilled")
    public OrderUnfilledsResponse getUnfilledOrders(
            HttpServletRequest request,
            @RequestParam(required = false) int stock_id,
            @RequestParam(required = false) String side,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) throws SecurityException {
        int userId = (int) request.getAttribute("user_id");
        return orderService.getUnfilledOrders(userId, stock_id,side,page,size, sort);
    }

    @GetMapping("/match")
    public OrderFilledsResponse getMatchs(
            HttpServletRequest request,
            @RequestParam(required = false) int stock_id,
            @RequestParam(required = false) String side,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sort
    ) throws SecurityException {
        int userId = (int) request.getAttribute("user_id");
        return orderService.getMatchs(userId, stock_id,side,page,size, sort);
    }

    @GetMapping("orderbook/{stock_id}")
    public OrderBookResponse getOrderBook(
            HttpServletRequest request, @PathVariable int stock_id) throws SecurityException {
        int userId = (int) request.getAttribute("user_id");
        return orderService.getOrderBook(userId,stock_id);
    }

}
