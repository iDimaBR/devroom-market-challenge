package com.github.idimabr.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class MarketData {

    private UUID itemId;
    private UUID sellerId;
    private String itemBase64;
    private double price;
    private long listedAt;

}
