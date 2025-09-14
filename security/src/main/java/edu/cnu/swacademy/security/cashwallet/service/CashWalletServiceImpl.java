package edu.cnu.swacademy.security.cashwallet.service;

import edu.cnu.swacademy.security.cashwallet.domain.CashWallet;
import edu.cnu.swacademy.security.cashwallet.domain.CashWalletHistory;
import edu.cnu.swacademy.security.cashwallet.dto.BalanceResponse;
import edu.cnu.swacademy.security.cashwallet.dto.CashWalletHistoriesResponse;
import edu.cnu.swacademy.security.cashwallet.dto.CashWalletHistoryResponse;
import edu.cnu.swacademy.security.cashwallet.repository.CashWalletHistoryRepository;
import edu.cnu.swacademy.security.cashwallet.repository.CashWalletRepository;
import edu.cnu.swacademy.security.common.BaseEntity;
import edu.cnu.swacademy.security.common.SecurityException;
import edu.cnu.swacademy.security.common.Validate;
import edu.cnu.swacademy.security.common.dml.RepositoryMapper;
import edu.cnu.swacademy.security.common.dml.SaveCommitter;
import edu.cnu.swacademy.security.common.worker.CommitWorker;
import edu.cnu.swacademy.security.common.worker.Worker;
import edu.cnu.swacademy.security.user.entity.User;
import edu.cnu.swacademy.security.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CashWalletServiceImpl implements CashWalletService{
    private final CashWalletRepository cashWalletRepository;
    private final CashWalletHistoryRepository cashWalletHistoryRepository;
    private final UserRepository userRepository;
    private final Validate validate;
    private final ApplicationContext context;


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
        validate.isBlock(cashWallet.isBlocked(),true);

        cashWallet.deposit(amount);
        cashWalletRepository.save(cashWallet);
        Worker worker = new CommitWorker(new SaveCommitter(context.getBean(RepositoryMapper.class))
                ,new BaseEntity[]{new CashWalletHistory("입금",amount,"거래사유",cashWallet.getReserve(),cashWallet)});
        worker.execute();
    }
    @Transactional
    @Override
    public void withdrawalWallet(int userId, int amount) throws SecurityException {
        User user = validate.isUser(userRepository.findById(userId));
        CashWallet cashWallet =validate.isCashWallet(cashWalletRepository.findByUserId(user.getId()));
        validate.isBlock(cashWallet.isBlocked(),true);

        validate.checkWithdrawal(cashWallet.getReserve()-cashWallet.getDeposit(),amount);
        cashWallet.withdrawal(amount);
        cashWalletRepository.save(cashWallet);
        Worker worker = new CommitWorker(new SaveCommitter(context.getBean(RepositoryMapper.class))
                ,new BaseEntity[]{new CashWalletHistory("출금",amount,"거래사유",cashWallet.getReserve(),cashWallet)});
        worker.execute();
    }

    @Override
    public BalanceResponse balance(int userId) throws SecurityException {
        User user = validate.isUser(userRepository.findById(userId));
        CashWallet cashWallet =validate.isCashWallet(cashWalletRepository.findByUserId(user.getId()));
        validate.isBlock(cashWallet.isBlocked(),true);

        return new BalanceResponse(cashWallet.getId(), (int) cashWallet.getReserve(), (int) cashWallet.getDeposit(), (int) (cashWallet.getReserve()-cashWallet.getDeposit()));
    }

    @Override
    public void cashWalletBlock(int cashWalletId) throws SecurityException {
        CashWallet cashWallet =validate.isCashWallet(cashWalletRepository.findById(cashWalletId));
        validate.isBlock(cashWallet.isBlocked(),true);

        cashWallet.blocked();
        cashWalletRepository.save(cashWallet);
    }

    @Override
    public void cashWalletUnBlock(int cashWalletId) throws SecurityException {
        CashWallet cashWallet =validate.isCashWallet(cashWalletRepository.findById(cashWalletId));
        validate.unBlock(cashWallet.isBlocked(),true);
        cashWallet.blocked();
        cashWalletRepository.save(cashWallet);
    }

    @Override
    public CashWalletHistoriesResponse cashWalletHistories(int userId, int page, int size, String sort) throws SecurityException {
        User user = validate.isUser(userRepository.findById(userId));
        CashWallet cashWallet =validate.isCashWallet(cashWalletRepository.findByUserId(user.getId()));
        Pageable pageable = sort.equals("desc") ? PageRequest.of(page, size, Sort.by("createdAt").descending()) :
                PageRequest.of(page, size, Sort.by("createdAt").ascending());
        Page<CashWalletHistory> histories = cashWalletHistoryRepository.findByCashWallet(pageable,cashWallet);
        List<CashWalletHistoryResponse> historyResponses = new ArrayList<>();
        for (CashWalletHistory cashWalletHistory: histories.getContent()) {
            historyResponses.add(new CashWalletHistoryResponse(cashWalletHistory));
        }
        return new CashWalletHistoriesResponse((int) histories.getTotalElements(),historyResponses);
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
