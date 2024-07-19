package com.evo.evoproject.domain.order;

import com.evo.evoproject.domain.product.RetrieveProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Data;
import java.time.LocalDateTime;

@Setter
@Getter
@Data
@NoArgsConstructor
public class Order {
    // Getters and Setters
    private int order_no;
    private int user_no;
    private int pro_no;
    private String order_name;
    private String order_address1;
    private String order_address2;
    private int order_phone;
    private String order_comment;
    private String pro_name;
    private LocalDateTime order_timestamp;
    private int pro_stock;
    private int order_payment;
    private int order_status;
    private int requestType;
    private int order_delivnum;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order{id=").append(pro_no)
                .append(", name='").append(pro_name).append('\'')
                .append(", address='").append(order_address1).append('\'')
                .append(", order_status=").append(order_status)
                .append(", products=[");

        sb.append("]}");
        return sb.toString();
    }
}
