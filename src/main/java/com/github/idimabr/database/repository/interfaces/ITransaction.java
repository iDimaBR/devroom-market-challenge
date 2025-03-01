package com.github.idimabr.database.repository.interfaces;

import org.bson.Document;
import java.util.Collection;
import java.util.UUID;

public interface ITransaction {

    void create(UUID seller, UUID buyer, String base64Item, double price);

    Collection<Document> get(UUID seller, UUID buyer);
}
