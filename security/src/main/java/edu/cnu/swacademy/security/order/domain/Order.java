package edu.cnu.swacademy.security.order.domain;

import edu.cnu.swacademy.security.common.BaseEntity;
import edu.cnu.swacademy.security.stock.domain.Stock;
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
@SQLDelete(sql = "UPDATE order SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "order")
@Entity
public class Order extends BaseEntity {
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

    @Column(nullable = false)
    private SideStatus side;
    @Column(nullable = false, columnDefinition  = "INT UNSIGNED")
    private int price;
    @Column(nullable = false , columnDefinition  = "INT UNSIGNED")
    private int amount;
    @Column(nullable = false , columnDefinition  = "INT UNSIGNED")
    private int unfilledAmount;
    @Column(nullable = false , columnDefinition  = "INT UNSIGNED")
    private int canceledAmount;
    public Order(User user,Stock stock,SideStatus side,  int price, int amount , int unfilledAmount ){
        this.user = user;
        this.stock = stock;
        this.side = side;
        this.price = price;
        this.amount = amount;
        this.unfilledAmount = unfilledAmount;
        this.canceledAmount =0;
    }




    public void cancel(){
        this.canceledAmount+=unfilledAmount;
        this.unfilledAmount = 0;
    }

    public void minusAmount(int amount){
        this.unfilledAmount-=amount;
    }
}
