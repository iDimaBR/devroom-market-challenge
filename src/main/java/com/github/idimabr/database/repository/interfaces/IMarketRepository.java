package com.github.idimabr.database.repository.interfaces;

import org.bson.Document;

import java.util.Collection;
import java.util.UUID;

public interface IMarketRepository {

    void create(UUID seller, String base64Item, double price);

    void delete(String id);

    void update(String id, String base64Item, double price);

    Document get(String id);

    Collection<Document> getAll();
}
