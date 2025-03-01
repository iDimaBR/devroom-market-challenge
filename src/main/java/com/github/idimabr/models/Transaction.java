package com.github.idimabr.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Transaction {

    private UUID transactionId;
    private UUID buyerId;
    private UUID sellerId;
    private UUID itemId;
    private double price;
    private long timestamp;
}
