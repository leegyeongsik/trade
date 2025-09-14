package edu.cnu.swacademy.security.order.service;

import edu.cnu.swacademy.security.cashwallet.domain.CashWallet;
import edu.cnu.swacademy.security.cashwallet.repository.CashWalletRepository;
import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.common.Validate;
import edu.cnu.swacademy.security.market.domain.MarketStatus;
import edu.cnu.swacademy.security.market.repository.MarketStatusRepository;
import edu.cnu.swacademy.security.market.service.TickSizeUtil;
import edu.cnu.swacademy.security.order.domain.Order;
import edu.cnu.swacademy.security.order.domain.SideStatus;
import edu.cnu.swacademy.security.order.dto.OrderRequest;
import edu.cnu.swacademy.security.order.repository.OrderRepository;
import edu.cnu.swacademy.security.stock.domain.Stock;
import edu.cnu.swacademy.security.stock.repository.StockRepository;
import edu.cnu.swacademy.security.stockwallet.domain.StockWallet;
import edu.cnu.swacademy.security.stockwallet.repository.StockWalletRepository;
import edu.cnu.swacademy.security.user.entity.User;
import edu.cnu.swacademy.security.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final Validate validate;
    private final UserRepository userRepository;
    private final CashWalletRepository cashWalletRepository;
    private final StockWalletRepository stockWalletRepository;
    private final StockRepository stockRepository;
    private final MarketStatusRepository marketStatusRepository;
    @Override
    @Transactional
    public void order(int userId, OrderRequest orderRequest) throws SecurityException {
        Stock stock = validate.isStock(stockRepository.findById(orderRequest.getStockId()));
        MarketStatus stockStatus =   marketStatusRepository.findTopByStockIdOrderByCreatedAtDesc(stock.getId());
        TickSizeUtil.valid(new BigDecimal(orderRequest.getPrice()),new BigDecimal(stockStatus.getReferencePrice()));
        User user = validate.isUser(userRepository.findById(userId));

        switch (SideStatus.valueOf(orderRequest.getSide()) ) {
            case BUY -> buyOrder(user, stock, orderRequest);
            case SELL -> sellOrder(user, stock, orderRequest);
        }
    }
    private void sellOrder(User user, Stock stock, OrderRequest orderRequest) throws SecurityException {
        StockWallet stockWallet = validate.isStockWallet(stockWalletRepository.findByUserAndStock(user,stock));
        validate.isBlock(stockWallet.isBlocked(),false);
        validate.possibleOrder(stockWallet.getReserve() , orderRequest.getQuantity());

        stockWallet.order(orderRequest.getQuantity());

        stockWalletRepository.save(stockWallet);
        Order order = new Order(user,stock, SideStatus.SELL,orderRequest.getPrice(),orderRequest.getQuantity(),orderRequest.getQuantity());
        orderRepository.save(order);

    }
    private void buyOrder(User user, Stock stock, OrderRequest orderRequest) throws SecurityException {
        CashWallet cashWallet =validate.isCashWallet(cashWalletRepository.findByUserId(user.getId()));
        validate.isBlock(cashWallet.isBlocked(),true);
        validate.possibleOrder(cashWallet.getReserve() , orderRequest.getPrice()*orderRequest.getQuantity());

        cashWallet.order(orderRequest.getPrice()*orderRequest.getQuantity());

        cashWalletRepository.save(cashWallet);

        Order order = new Order(user,stock, SideStatus.BUY,orderRequest.getPrice(),orderRequest.getQuantity(),orderRequest.getQuantity());
        orderRepository.save(order);
    }
}
