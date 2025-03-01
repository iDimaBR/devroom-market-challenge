package com.github.idimabr.database.repository.interfaces;

import org.bson.Document;
import java.util.Collection;

public interface IBlackMarketRepository {

    void generate(Collection<Document> items);

    Collection<Document> getBlackMarket();

    void purgeExpired();
}
