package com.github.idimabr.database.repository.interfaces;

import org.bson.Document;
import java.util.Collection;
import java.util.UUID;

public interface IMarketRepository {

    void create(UUID id, UUID seller, String base64Item, double price);

    void delete(UUID id);

    void update(UUID id, String base64Item, double price);

    Document get(UUID id);

    Collection<Document> getAll();
}
