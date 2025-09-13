package edu.cnu.swacademy.security.cashwallet.controller;

import edu.cnu.swacademy.security.cashwallet.dto.BalanceResponse;
import edu.cnu.swacademy.security.cashwallet.dto.CashWalletHistoriesResponse;
import edu.cnu.swacademy.security.cashwallet.dto.DepositAndWithdrawalRequest;
import edu.cnu.swacademy.security.cashwallet.service.CashWalletService;
import edu.cnu.swacademy.security.common.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cash-wallet")
public class CashWalletController {
    private final CashWalletService cashWalletService;
    public CashWalletController(CashWalletService cashWalletService) {
        this.cashWalletService = cashWalletService;
    }

    @PostMapping()
    public void createCashWallet(HttpServletRequest request) throws Exception {
        int userId = (int) request.getAttribute("user_id");
        cashWalletService.createCashWallet(userId);
    }
    @PostMapping("/deposit")
    public void depositWallet(HttpServletRequest request , @Valid @RequestBody DepositAndWithdrawalRequest depositAndWithdrawalRequest) throws Exception {
        int userId = (int) request.getAttribute("user_id");
        cashWalletService.depositWallet(userId, depositAndWithdrawalRequest.getAmount());
    }
    @PostMapping("/withdrawal")
    public void withdrawalWallet(HttpServletRequest request , @Valid @RequestBody    DepositAndWithdrawalRequest depositAndWithdrawalRequest) throws Exception {
        int userId = (int) request.getAttribute("user_id");
        cashWalletService.withdrawalWallet(userId,depositAndWithdrawalRequest.getAmount());
    }
    @GetMapping("/balance")
    public BalanceResponse balance(HttpServletRequest request) throws SecurityException {
        int userId = (int) request.getAttribute("user_id");
        return cashWalletService.balance(userId);
    }
    @GetMapping("/histories")
    public CashWalletHistoriesResponse cashWalletHistories(HttpServletRequest request,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "20")int size,
                                                           @RequestParam(defaultValue = "desc")String sort) throws Exception {
        int userId = (int) request.getAttribute("user_id");
        return cashWalletService.cashWalletHistories(userId,page,size,sort);
    }

    @PostMapping("/cash-wallet/{userId}/block")
    public void cashWalletBlock( @PathVariable int userId) throws Exception {
        cashWalletService.cashWalletBlock(userId);
    }
    @PostMapping("/cash-wallet/{userId}/unblock")
    public void cashWalletUnBlock( @PathVariable int userId) throws Exception {
        cashWalletService.cashWalletUnBlock(userId);
    }

}
