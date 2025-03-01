package com.github.idimabr.database.repository.impl;

import com.github.idimabr.database.connection.MongoDBConnection;
import com.github.idimabr.database.repository.interfaces.IPlayerRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bukkit.Bukkit;
import java.util.ArrayList;
import java.util.UUID;

public class PlayerRepository implements IPlayerRepository {

    private final MongoCollection<Document> collection;

    public PlayerRepository(MongoDBConnection resource) {
        this.collection = resource.getDatabase().getCollection("players");
    }

    @Override
    public void save(UUID player) {
        final Document existingPlayer = collection.find(Filters.eq("uuid", player.toString())).first();
        if (existingPlayer == null) {
            Document doc = new Document("uuid", player.toString())
                    .append("name", Bukkit.getOfflinePlayer(player).getName())
                    .append("balance", 0.0)
                    .append("transactions", new ArrayList<>());

            collection.insertOne(doc);
            return;
        }

        update(player);
    }

    @Override
    public void update(UUID player) {
        collection.updateOne(
                Filters.eq("uuid", player.toString()),
                Updates.set("name", Bukkit.getOfflinePlayer(player).getName())
        );
    }

    @Override
    public Document get(UUID player) {
        return collection.find(Filters.eq("uuid", player.toString())).first();
    }

    @Override
    public void delete(UUID player) {
        collection.deleteOne(Filters.eq("uuid", player.toString()));
    }

    public void updateBalance(UUID playerId, double newBalance) {
        collection.updateOne(
                Filters.eq("uuid", playerId.toString()), Updates.set("balance", newBalance)
        );
    }

    public void addTransaction(UUID playerId, UUID transactionId) {
        collection.updateOne(
                Filters.eq("uuid", playerId.toString()), Updates.push("transactions", transactionId.toString())
        );
    }
}