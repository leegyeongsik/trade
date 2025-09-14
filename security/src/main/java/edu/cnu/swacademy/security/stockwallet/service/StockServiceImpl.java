package edu.cnu.swacademy.security.stockwallet.service;

import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.common.Validate;
import edu.cnu.swacademy.security.stock.domain.Stock;
import edu.cnu.swacademy.security.stock.repository.StockRepository;
import edu.cnu.swacademy.security.stockwallet.domain.StockWallet;
import edu.cnu.swacademy.security.stockwallet.dto.BalanceResponse;
import edu.cnu.swacademy.security.stockwallet.repository.StockWalletRepository;
import edu.cnu.swacademy.security.user.entity.User;
import edu.cnu.swacademy.security.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class StockServiceImpl implements StockService{
    private final StockWalletRepository stockWalletRepository;
    private final Validate validate;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;


    @Override
    @Transactional
    public void createStockWallet(int userId, int stockId) throws SecurityException {
        User user = validate.isUser(userRepository.findById(userId));
        Stock stock = validate.isStock(stockRepository.findById(stockId));

        stockWalletRepository.save(new StockWallet(user,stock));
    }

    @Override
    public void depositWallet(int stockWalletId, int amount) throws SecurityException {
        StockWallet stockWallet = validate.isStockWallet(stockWalletRepository.findById(stockWalletId));
        validate.isBlock(stockWallet.isBlocked(),false);

        stockWallet.deposit(amount);
        stockWalletRepository.save(stockWallet);
    }

    @Override
    public BalanceResponse balance(int userId, int stockId) throws SecurityException {
        User user = validate.isUser(userRepository.findById(userId));
        Stock stock = validate.isStock(stockRepository.findById(stockId));
        StockWallet stockWallet = validate.isStockWallet(stockWalletRepository.findByUserAndStock(user,stock));
        validate.isBlock(stockWallet.isBlocked(),false);

        return new BalanceResponse(stockWallet.getId(),
                stockWallet.getStock().getId(),
                stockWallet.getDeposit(),
                stockWallet.getDeposit(),
                stockWallet.getReserve()-stockWallet.getDeposit());
    }

    @Override
    public void WalletBlock(int stockWalletId) throws SecurityException {
        StockWallet stockWallet = validate.isStockWallet(stockWalletRepository.findById(stockWalletId));
        validate.isBlock(stockWallet.isBlocked(),false);

        stockWallet.blocked();
        stockWalletRepository.save(stockWallet);
    }

    @Override
    public void WalletUnBlock(int stockWalletId) throws SecurityException {
        StockWallet stockWallet = validate.isStockWallet(stockWalletRepository.findById(stockWalletId));
        validate.unBlock(stockWallet.isBlocked(),false);

        stockWallet.blocked();
        stockWalletRepository.save(stockWallet);
    }
}
