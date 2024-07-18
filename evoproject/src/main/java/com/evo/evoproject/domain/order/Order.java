package com.evo.evoproject.domain.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Order {
    // Getters and Setters
    private Long id;
    private String user_name;
    private String item;
    private int quantity;
    private int price;
    private String status;
}
