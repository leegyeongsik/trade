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
@SQLDelete(sql = "UPDATE stock SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Table(name = "stock")
@Entity
public class Stock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private int id;
    @Column(nullable = false, length = 255  , unique = true)
    private String name;
    @Column(nullable = false, length = 6 , unique = true)
    private String code;
    public Stock(String name, String code){
        this.name =  name;
        this.code  =code;
    }
}
