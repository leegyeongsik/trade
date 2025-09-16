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
@SQLDelete(sql = "UPDATE match SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "match")
@Entity
public class Match extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Stock stock;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Order makerOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Order takerOrder;

    public Match(Stock stock , Order makerOrder , Order  takerOrder ){
        this.stock = stock;
        this.makerOrder = makerOrder;
        this.takerOrder = takerOrder;
    }

}
