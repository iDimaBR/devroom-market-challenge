package com.github.idimabr;

import com.github.idimabr.database.connection.MongoDBConnection;
import com.github.idimabr.database.repository.impl.BlackMarketRepository;
import com.github.idimabr.database.repository.impl.MarketplaceRepository;
import com.github.idimabr.database.repository.impl.PlayerRepository;
import com.github.idimabr.database.repository.impl.TransactionRepository;
import com.github.idimabr.hooks.DiscordHook;
import com.github.idimabr.utils.ConfigUtil;
import com.google.gson.Gson;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class DevRoomMarket extends JavaPlugin {

    private ConfigUtil config;
    private MongoDBConnection database;
    private PlayerRepository playerRepository;
    private TransactionRepository transactionRepository;
    private BlackMarketRepository blackMarketRepository;
    private MarketplaceRepository marketplaceRepository;
    private DiscordHook discordHook;

    public static Gson GSON = new Gson();

    @Override
    public void onEnable() {
        loadDatabase();
        loadHooks();
    }

    @Override
    public void onDisable() {
        unloadDatabase();
    }

    private void loadDatabase() {
        database = new MongoDBConnection(this);
        playerRepository = new PlayerRepository(database);
        transactionRepository = new TransactionRepository(database);
        blackMarketRepository = new BlackMarketRepository(database);
        marketplaceRepository = new MarketplaceRepository(database);
    }

    private void loadHooks(){
        if(config.getBoolean("discord.enabled"))
            discordHook = new DiscordHook(this);
    }

    private void unloadDatabase(){
        if(database != null)
            database.disconnect();
    }
}
