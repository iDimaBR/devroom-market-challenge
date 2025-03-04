package com.github.idimabr.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class Transaction {

    private UUID buyerId;
    private UUID sellerId;
    private UUID itemId;
    private double price;
    private long timestamp;

    public String getBuyerName(){
        return Bukkit.getOfflinePlayer(buyerId).getName();
    }

    public String getSellerName(){
        return Bukkit.getOfflinePlayer(sellerId).getName();
    }

    public String getListed() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final Date date = new Date(timestamp);
        return sdf.format(date);
    }
}
