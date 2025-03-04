package com.github.idimabr.tasks;

import com.github.idimabr.controller.MarketController;
import com.github.idimabr.models.MarketData;
import lombok.AllArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public class BlackMarketTask extends BukkitRunnable {

    private MarketController marketController;

    @Override
    public void run() {
        final List<MarketData> marketData = marketController.getMarketData();
        final List<MarketData> blackMarket = marketController.getBlackMarket();

        int itemsToSelect = Math.max(1, (marketData.size() * 30) / 100);
        itemsToSelect = Math.min(itemsToSelect, marketData.size());

        while (blackMarket.size() < itemsToSelect) {
            final MarketData randomItem = marketData.get(ThreadLocalRandom.current().nextInt(marketData.size()));

            if (!blackMarket.contains(randomItem)) {
                blackMarket.add(randomItem);
            }
        }
    }
}