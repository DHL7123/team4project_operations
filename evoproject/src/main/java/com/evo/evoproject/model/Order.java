package com.evo.evoproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Order {
    // Getters and Setters
    private Long id;
    private String userName;
    private String item;
    private int quantity;
    private double price;

}
