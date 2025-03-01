package com.github.idimabr.database.repository.impl;

import com.github.idimabr.database.connection.MongoDBConnection;
import com.github.idimabr.database.repository.interfaces.IMarketRepository;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class MarketplaceRepository implements IMarketRepository {
    private final MongoCollection<Document> collection;

    public MarketplaceRepository(MongoDBConnection database) {
        this.collection = database.getDatabase().getCollection("marketplace");
    }

    @Override
    public void create(UUID itemID, UUID seller, String base64Item, double price) {
        Document doc = new Document("seller", seller.toString())
                .append("itemID", itemID.toString())
                .append("item", base64Item)
                .append("price", price)
                .append("listed_at", System.currentTimeMillis());
        collection.insertOne(doc);
    }

    @Override
    public void delete(UUID id) {
        collection.deleteOne(Filters.eq("itemID", id.toString()));
    }

    @Override
    public void update(UUID id, String base64Item, double price) {
        final Document document = get(id);
        if(document == null) return;

        document.append("item", base64Item);
        document.append("price", price);
        collection.updateOne(
                Filters.eq("itemID", id.toString()),
                document
        );
    }

    @Override
    public Document get(UUID id) {
        return collection.find(Filters.eq("itemID", id.toString())).first();
    }

    @Override
    public Collection<Document> getAll() {
        return collection.find().into(new ArrayList<>());
    }
}