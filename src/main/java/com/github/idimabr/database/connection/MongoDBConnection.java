package com.github.idimabr.database.connection;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class MongoDBConnection {

    private final JavaPlugin plugin;
    private MongoClient mongoClient;
    private MongoDatabase database;

    public MongoDBConnection(JavaPlugin plugin) {
        this.plugin = plugin;
        connect();
    }

    public void connect() {
        try {
            final FileConfiguration config = plugin.getConfig();
            final String uri = config.getString("mongodb.uri", "mongodb://localhost:27017");
            final String dbName = config.getString("mongodb.database", "devroom");

            mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase(dbName);
            plugin.getLogger().info("Connected to " + dbName);
        } catch (Exception e) {
            plugin.getLogger().severe("Error in connection!");
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (mongoClient != null) {
            mongoClient.close();
            plugin.getLogger().info("Connection closed");
        }
    }

    public MongoDatabase getDatabase() {
        return database;
    }
}
