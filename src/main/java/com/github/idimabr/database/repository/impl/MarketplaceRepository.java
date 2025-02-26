package com.github.idimabr.database.repository.impl;

import com.github.idimabr.database.repository.interfaces.IMarketRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MarketplaceRepository implements IMarketRepository {
    private final MongoCollection<Document> collection;

    public MarketplaceRepository(MongoDatabase database) {
        this.collection = database.getCollection("marketplace");
    }

    @Override
    public void create(UUID seller, String base64Item, double price) {
        Document doc = new Document("seller", seller.toString())
                .append("item", base64Item)
                .append("price", price)
                .append("listed_at", System.currentTimeMillis());
        collection.insertOne(doc);
    }

    @Override
    public void delete(String id) {
        collection.deleteOne(Filters.eq("_id", id));
    }

    @Override
    public void update(String id, String base64Item, double price) {
        final Document document = get(id);
        if(document == null) return;

        document.append("item", base64Item);
        document.append("price", price);
        collection.updateOne(
                Filters.eq("_id", id),
                document
        );
    }

    @Override
    public Document get(String id) {
        return collection.find(Filters.eq("_id", id)).first();
    }

    @Override
    public Collection<Document> getAll() {
        return collection.find().into(new ArrayList<>());
    }
}