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
    public void create(UUID seller, UUID buyer, String base64Item, double price) {
        Document doc = new Document("buyer", buyer.toString())
                .append("seller", seller.toString())
                .append("item", base64Item)
                .append("price", price)
                .append("created_at", System.currentTimeMillis());
        collection.insertOne(doc);
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
