package edu.cnu.swacademy.exchange.domain;

import edu.cnu.swacademy.exchange.common.BaseEntity;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Stock stock;

    @Column(nullable = false, columnDefinition  = "INT UNSIGNED" )
    private int reserve;
    @Column(nullable = false, columnDefinition  = "INT UNSIGNED")
    private int deposit;
    @Column(nullable = false)
    private boolean isBlocked;

    public StockWallet(User user,Stock stock){
        this.user = user;
        this.stock = stock;
        this.deposit = 0;
        this.reserve= 0;
    }
    public void deposit(int amount){
        this.reserve+=amount;
    }
    public void blocked(){
        this.isBlocked = !this.isBlocked;
    }

    public void order( int quantity) {
        this.reserve-=quantity;
        this.deposit+=quantity;
    }
}
