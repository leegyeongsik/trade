package edu.cnu.swacademy.security.cashwallet.service;

import edu.cnu.swacademy.security.cashwallet.domain.CashWallet;
import edu.cnu.swacademy.security.cashwallet.dto.BalanceResponse;
import edu.cnu.swacademy.security.cashwallet.repository.CashWalletRepository;
import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.common.Validate;
import edu.cnu.swacademy.security.user.entity.User;
import edu.cnu.swacademy.security.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CashWalletServiceImpl implements CashWalletService{
    private final CashWalletRepository cashWalletRepository;
    private final UserRepository userRepository;
    private final Validate validate;
    public CashWalletServiceImpl(CashWalletRepository cashWalletRepository, UserRepository userRepository, Validate validate) {
        this.cashWalletRepository = cashWalletRepository;
        this.userRepository = userRepository;
        this.validate = validate;
    }

    @Override
    public void createCashWallet(int userId) throws SecurityException {
        User user = validate.isUser(userRepository.findById(userId));
        String walletNumber =  transWallet();
        CashWallet cashWallet = new CashWallet(user,walletNumber);
        cashWalletRepository.save(cashWallet);
    }
    @Transactional
    @Override
    public void depositWallet(int userId, int amount) throws SecurityException {
        User user = validate.isUser(userRepository.findById(userId));
        CashWallet cashWallet =validate.isCashWallet(cashWalletRepository.findByUserId(user.getId()));
        cashWallet.deposit(amount);
        cashWalletRepository.save(cashWallet);
    }
    @Transactional
    @Override
    public void withdrawalWallet(int userId, int amount) throws SecurityException {
        User user = validate.isUser(userRepository.findById(userId));
        CashWallet cashWallet =validate.isCashWallet(cashWalletRepository.findByUserId(user.getId()));
        validate.check(cashWallet.getReserve()-cashWallet.getDeposit(),amount);
        cashWallet.withdrawal(amount);
        cashWalletRepository.save(cashWallet);
    }

    @Override
    public BalanceResponse balance(int userId) throws SecurityException {
        User user = validate.isUser(userRepository.findById(userId));
        CashWallet cashWallet =validate.isCashWallet(cashWalletRepository.findByUserId(user.getId()));
        return new BalanceResponse(cashWallet.getId(), (int) cashWallet.getReserve(), (int) cashWallet.getDeposit(), (int) (cashWallet.getReserve()-cashWallet.getDeposit()));
    }

    @Override
    public void cashWalletBlock(int userId) throws SecurityException {
        CashWallet cashWallet =validate.isCashWallet(cashWalletRepository.findByUserId(userId));

        cashWallet.blocked();
        cashWalletRepository.save(cashWallet);
    }

    @Override
    public void cashWalletUnBlock(int userId) throws SecurityException {
        CashWallet cashWallet =validate.isCashWallet(cashWalletRepository.findByUserId(userId));
        validate.unBlock();

        cashWallet.blocked();
        cashWalletRepository.save(cashWallet);
    }

    private String transWallet() {
        int[] wallet = new int[12];
        wallet[0] =7;
        wallet[1] = 7;
        wallet[2] = 7;
        wallet[11] = 1;
        Random random = new Random();
        for (int i = 3; i < 11; i++) {
            wallet[i] = random.nextInt(0,10);
        }
        StringBuilder number = new StringBuilder();
        for (int i : wallet) {
            number.append(i);
        }
        return number.toString();
    }
}
