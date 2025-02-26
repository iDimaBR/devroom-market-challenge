package com.github.idimabr.database.repository.impl;

import com.github.idimabr.database.repository.interfaces.IBlackMarketRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class BlackMarketRepository implements IBlackMarketRepository {
    private final MongoCollection<Document> collection;

    public BlackMarketRepository(MongoDatabase database) {
        this.collection = database.getCollection("blackmarket");
    }

    public void generate(Collection<Document> items) {
        List<Document> blackMarketItems = items.stream().map(item -> new Document("original_id", item.getObjectId("_id"))
                .append("item", item.getString("item"))
                .append("original_price", item.getDouble("price"))
                .append("discounted_price", item.getDouble("price") * 0.5)
                .append("expires_at", System.currentTimeMillis() + 86400000))
                .collect(Collectors.toList());
        collection.insertMany(blackMarketItems);
    }

    public Collection<Document> getBlackMarket() {
        return collection.find().into(new ArrayList<>());
    }

    public void purgeExpired() {
        collection.deleteMany(
                Filters.lt("expires_at", System.currentTimeMillis())
        );
    }
}