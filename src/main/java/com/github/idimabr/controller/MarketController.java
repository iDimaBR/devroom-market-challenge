package com.github.idimabr.controller;

import com.github.idimabr.models.MarketData;
import lombok.Getter;
import org.bson.Document;

import java.util.*;

@Getter
public class MarketController {

    private List<MarketData> marketData = new ArrayList<>();
    private List<MarketData> blackMarket = new ArrayList<>();

    public void register(MarketData data) {
        marketData.add(data);
    }

    public void unregister(MarketData data) {
        marketData.remove(data);
    }

    public void load(Collection<Document> all) {
        all.forEach(document -> {
            final UUID itemID = UUID.fromString(document.getString("itemID"));
            final UUID seller = UUID.fromString(document.getString("seller"));
            final String item = document.getString("item");
            final double price = document.getDouble("price");
            final long listedAt = document.getLong("listed_at");

            final MarketData data = new MarketData(itemID, seller, item, price, listedAt);
            register(data);
        });
    }
}
