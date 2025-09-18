package edu.cnu.swacademy.security.order.controller;

import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.order.dto.ExchangeDto;
import edu.cnu.swacademy.security.order.dto.OrderRequest;
import edu.cnu.swacademy.security.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping()
    void order(HttpServletRequest request , @Valid @RequestBody OrderRequest orderRequest) throws SecurityException, IOException {
        int userId = (int) request.getAttribute("user_id");
        orderService.order(userId, orderRequest);
    }

    @PostMapping("/result")
    void result(@RequestBody ExchangeDto exchangeDto){
        orderService.orderResult(exchangeDto);
    }

    @GetMapping("/{orderId}")
    void orderCancel(HttpServletRequest request, @PathVariable int orderId ){
        int userId = (int) request.getAttribute("user_id");
        orderService.orderCancel(userId,orderId);
    }

}
