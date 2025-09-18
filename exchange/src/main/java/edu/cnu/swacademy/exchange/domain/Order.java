package edu.cnu.swacademy.exchange.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order  {
    private int order_id;
    private int product_id;
    private int price;
    private int amount;
    private String side;
    private String created_at;

    public Order(int order_id , int product_id , int price,int amount , String side , String created_at){
        this.order_id = order_id;
        this.product_id = product_id;
        this.price = price;
        this.amount = amount;
        this.side = side;
        this.created_at = created_at;
    }
}
