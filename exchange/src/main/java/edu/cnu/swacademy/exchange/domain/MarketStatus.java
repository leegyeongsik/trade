package edu.cnu.swacademy.exchange.domain;

import edu.cnu.swacademy.exchange.common.BaseEntity;
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
@SQLDelete(sql = "UPDATE market_status SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "market_status")
@Entity
public class MarketStatus extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Stock stock;


    @Column(nullable = false, length = 500, unique = true)
    private LocalDateTime tradingDate;


    @Column(nullable = false, columnDefinition  = "INT UNSIGNED")
    private int referencePrice;
    @Column(nullable = false, columnDefinition  = "INT UNSIGNED")
    private int upperLimitPrice;
    @Column(nullable = false, columnDefinition  = "INT UNSIGNED")
    private int lowerLimitPrice;
    @Column(columnDefinition  = "INT UNSIGNED")
    private int openingPrice;
    @Column(columnDefinition  = "INT UNSIGNED")
    private int closingPrice;
    @Column(columnDefinition  = "INT UNSIGNED")
    private int highestPrice;
    @Column(columnDefinition  = "INT UNSIGNED")
    private int lowestPrice;
    @Column(columnDefinition  = "INT UNSIGNED")
    private int tradingVolume;
    @Column(columnDefinition  = "BIGINT UNSIGNED")
    private long tradingAmount;

    public MarketStatus(Stock stock ,
                        LocalDateTime tradingDate ,
                        int referencePrice,
                        int upperLimitPrice,
                        int lowerLimitPrice){
        this.stock = stock;
        this.tradingDate  = tradingDate;
        this.referencePrice = referencePrice;
        this.upperLimitPrice = upperLimitPrice;
        this.lowerLimitPrice = lowerLimitPrice;
        this.openingPrice = 0;
        this.closingPrice = 0;
        this.highestPrice = 0;
        this.lowestPrice = 0;
        this.tradingVolume = 0;
        this.tradingAmount = 0;
    }
}
