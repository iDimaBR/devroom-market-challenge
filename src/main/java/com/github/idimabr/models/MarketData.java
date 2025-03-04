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
public class MarketData {

    private UUID itemId;
    private UUID sellerId;
    private String itemBase64;
    private double price;
    private long listedAt;

    public String getSellerName(){
        return Bukkit.getOfflinePlayer(sellerId).getName();
    }

    public String getListed() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final Date date = new Date(listedAt);
        return sdf.format(date);
    }

}
