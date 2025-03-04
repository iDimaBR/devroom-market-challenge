package com.github.idimabr.database.repository.impl;

import com.github.idimabr.database.connection.MongoDBConnection;
import com.github.idimabr.database.repository.interfaces.ITransaction;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class TransactionRepository implements ITransaction {
    private final MongoCollection<Document> collection;

    public TransactionRepository(MongoDBConnection database) {
        this.collection = database.getDatabase().getCollection("transactions");
    }

    @Override
    public void create(UUID seller, UUID buyer, UUID itemID, double price, long createdAt) {
        Document doc = new Document("buyer", buyer.toString())
                .append("seller", seller.toString())
                .append("itemID", itemID.toString())
                .append("price", price)
                .append("created_at", createdAt);
        collection.insertOne(doc);
    }

    public Collection<Document> get(UUID uuid) {
        return collection.find(
                Filters.or(
                        Filters.eq("buyer", uuid.toString()),
                        Filters.eq("seller", uuid.toString())
                )
        ).into(new ArrayList<>());
    }

    @Override
    public Collection<Document> get(UUID seller, UUID buyer) {
        return collection.find(
                Filters.or(
                        Filters.eq("buyer", buyer.toString()),
                        Filters.eq("seller", seller.toString())
                )
        ).into(new ArrayList<>());
    }
}
