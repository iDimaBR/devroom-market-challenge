package com.github.idimabr.database.repository.interfaces;

import org.bson.Document;
import java.util.UUID;

public interface IPlayerRepository {

    void save(UUID player);

    void update(UUID player);

    Document get(UUID player);

    void delete(UUID player);

    void updateBalance(UUID player, double balance);

    void addTransaction(UUID player, UUID transaction);
}
