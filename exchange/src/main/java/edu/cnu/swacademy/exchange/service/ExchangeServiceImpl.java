package edu.cnu.swacademy.exchange.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.cnu.swacademy.exchange.domain.Order;
import edu.cnu.swacademy.exchange.dto.*;
import edu.cnu.swacademy.exchange.process.WorkerQueue;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

import static edu.cnu.swacademy.exchange.dto.ExchangeResponse.exchangeIng;
import static edu.cnu.swacademy.exchange.dto.ExchangeResponse.reject;

@Service
public class ExchangeServiceImpl implements ExchangeService {
    private final WorkerQueue workerQueue;
    private final RestTemplate restTemplate = new RestTemplate();
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    public ExchangeServiceImpl(WorkerQueue workerQueue, StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.workerQueue = workerQueue;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }
    @Value("${security.server.port}")
    private int securityServerPort;

    @Value("${security.server.host}")
    private String securityServerHost;

    @Override
    public ExchangeResponse orderProcess(@Valid @RequestBody OrderRequest orderRequest) {
        workerQueue.orderOffer(new Order(orderRequest.getOrderId(),
                orderRequest.getProductId(),
                orderRequest.getPrice(),
                orderRequest.getAmount(),
                orderRequest.getSide(),
                orderRequest.getCreatedAt()));
        return exchangeIng();
    }

    @Override
    public ExchangeResponse orderDeleteProcess(OrderDeleteRequest orderDeleteRequest) {
         RedisOrderDto redisOrderDto  = getOrder(orderDeleteRequest.getOrderId());
        return  redisOrderDto == null ? reject():
                new ExchangeResponse(OrderStatus.Cancelled.name(), redisOrderDto.getOrderId(), -1, redisOrderDto.getUnfilledUnit());


    }

    public void orderComplete(ExchangeResponse exchangeResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = String.format("http://%s:%s/api/v1/order/result", securityServerHost, securityServerPort);
        HttpEntity<ExchangeResponse> requestEntity = new HttpEntity<>(exchangeResponse, headers);

        restTemplate.postForEntity(url, requestEntity, ExchangeResponse.class);
    }

    public RedisOrderDto getOrder(int targetOrderId){
        Set<String> keys = redisTemplate.keys("*:*:*");

        for (String key : keys) {
            List<String> orders = redisTemplate.opsForList().range(key, 0, -1);
            for (String orderJson : orders) {
                RedisOrderDto order;
                try {
                    order = objectMapper.readValue(orderJson, RedisOrderDto.class);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
                if (order.getOrderId() == targetOrderId) {
                    redisTemplate.opsForList().remove(key, 1, orderJson);
                    return order;
                }
            }
        }
        return null;
    }
}
