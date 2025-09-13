package edu.cnu.swacademy.security.stockwallet.domain;

import edu.cnu.swacademy.security.common.BaseEntity;
import edu.cnu.swacademy.security.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Getter
@AllArgsConstructor
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE stock_wallet SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "stock_wallet")
@Entity

public class StockWallet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private int id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Column(columnDefinition = "INT UNSIGNED",unique = true,nullable = false)
    private int stockId;

    @Column(nullable = false, columnDefinition  = "INT UNSIGNED" )
    private int reserve;
    @Column(nullable = false, columnDefinition  = "INT UNSIGNED")
    private int deposit;
    @Column(nullable = false)
    private boolean isBlocked;

    public StockWallet(User user, int stockId){
        this.user = user;
        this.stockId = stockId;
        this.deposit = 0;
        this.reserve= 0;
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
}
