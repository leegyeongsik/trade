package edu.cnu.swacademy.security.cashwallet.domain;

import edu.cnu.swacademy.security.common.BaseEntity;
import edu.cnu.swacademy.security.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE cash_wallet SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "cash_wallet")
@Entity

public class CashWallet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private int id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false, length = 500, unique = true)
    private String accountNumber;

    @Column(nullable = false, columnDefinition  = "BIGINT UNSIGNED")
    private long reserve;
    @Column(nullable = false, columnDefinition  = "BIGINT UNSIGNED")
    private long deposit;
    @Column(nullable = false)
    private boolean isBlocked;

    public CashWallet(User user, String accountNumber){
        this.user = user;
        this.accountNumber = accountNumber;
    }
    public void deposit(int amount){
        this.reserve+=amount;
    }
    public void withdrawal(int amount) {
        this.reserve-=amount;
    }

    public void blocked(){
        this.isBlocked = !this.isBlocked;
    }


    public void order(int price){
        this.reserve-=price;
        this.deposit+=price;
    }
}
