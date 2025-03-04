package com.github.idimabr.database.repository.interfaces;

import org.bson.Document;
import java.util.Collection;
import java.util.UUID;

public interface ITransaction {

    void create(UUID seller, UUID buyer, UUID itemID, double price, long createdAt);

    Collection<Document> get(UUID seller, UUID buyer);
}
