package com.github.idimabr.controller;

import com.github.idimabr.models.MarketData;
import java.util.*;

public class MarketController {
    private List<MarketData> marketData = new ArrayList<>();

    public void register(MarketData data) {
        marketData.add(data);
    }

    public void unregister(MarketData data) {
        marketData.remove(data);
    }

    public List<MarketData> getMarketData() {
        return marketData;
    }
}
