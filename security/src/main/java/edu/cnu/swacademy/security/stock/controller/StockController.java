package edu.cnu.swacademy.security.stock.controller;

import edu.cnu.swacademy.security.cashwallet.dto.DepositAndWithdrawalRequest;
import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.stock.dto.BalanceResponse;
import edu.cnu.swacademy.security.stock.dto.StockCreateRequest;
import edu.cnu.swacademy.security.stock.dto.StockReserveRequest;
import edu.cnu.swacademy.security.stock.service.StockService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/stock-wallet")

public class StockController {
    private final StockService stockService;
    @PostMapping()
    public void createStockWallet(HttpServletRequest request , @Valid @RequestBody StockCreateRequest stockCreateRequest) throws Exception {
        int userId = (int) request.getAttribute("user_id");
        stockService.createStockWallet(userId,stockCreateRequest.getStockId());
    }
    @PostMapping("/reserve")
    public void depositWallet( @Valid @RequestBody StockReserveRequest stockReserveRequest) throws Exception {
        stockService.depositWallet(stockReserveRequest.getStockWalletId(),stockReserveRequest.getAmount());
    }
    @GetMapping("/balance/{stock_id}")
    public BalanceResponse balance(HttpServletRequest request, @PathVariable int stock_id) throws SecurityException {
        int userId = (int) request.getAttribute("user_id");
        return stockService.balance(userId,stock_id);
    }
    @PostMapping("/{stock_wallet_id}/block")
    public void WalletBlock( @PathVariable int stock_wallet_id) throws Exception {
        stockService.WalletBlock(stock_wallet_id);
    }
    @PostMapping("/{stock_wallet_id}/unblock")
    public void WalletUnBlock( @PathVariable int stock_wallet_id) throws Exception {
        stockService.WalletUnBlock(stock_wallet_id);
    }
}
