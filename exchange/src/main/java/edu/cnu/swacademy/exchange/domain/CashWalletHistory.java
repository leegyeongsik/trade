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
@SQLDelete(sql = "UPDATE cash_wallet_history SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "cash_wallet_history")
@Entity
public class CashWalletHistory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private CashWallet cashWallet;

    @Column(nullable = false, length = 20)
    private String txType;

    @Column(nullable = false , columnDefinition  = "BIGINT UNSIGNED")
    private long txAmount;

    @Column(nullable = false , length = 100)
    private String txNote;

    @Column(nullable = false, columnDefinition  = "BIGINT UNSIGNED")
    private long reserve;

    public CashWalletHistory( String txType ,long txAmount , String txNote , long reserve, CashWallet cashWallet){
        this.txType = txType;
        this.txAmount = txAmount;
        this.txNote = txNote;
        this.reserve = reserve;
        this.cashWallet = cashWallet;
    }
}
