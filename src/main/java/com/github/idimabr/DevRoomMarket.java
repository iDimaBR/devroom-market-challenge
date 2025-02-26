package com.github.idimabr;

import com.github.idimabr.database.connection.MongoDBConnection;
import com.github.idimabr.database.repository.impl.PlayerRepository;
import org.bukkit.plugin.java.JavaPlugin;

public final class DevRoomMarket extends JavaPlugin {

    private MongoDBConnection database;
    private PlayerRepository playerRepository;

    @Override
    public void onEnable() {
        loadDatabase();
    }

    @Override
    public void onDisable() {
        unloadDatabase();
    }

    private void loadDatabase() {
        database = new MongoDBConnection(this);
        playerRepository = new PlayerRepository(database);
    }

    private void unloadDatabase(){
        if(database != null)
            database.disconnect();
    }
}
