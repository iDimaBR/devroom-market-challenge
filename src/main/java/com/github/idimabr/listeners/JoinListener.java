package com.github.idimabr.listeners;

import com.github.idimabr.controller.PlayerController;
import com.github.idimabr.database.repository.impl.PlayerRepository;
import com.github.idimabr.database.repository.impl.TransactionRepository;
import com.github.idimabr.models.PlayerData;
import com.github.idimabr.models.Transaction;
import com.github.idimabr.utils.ItemSerializer;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Collection;
import java.util.UUID;

@AllArgsConstructor
public class JoinListener implements Listener {

    private PlayerRepository playerRepository;
    private PlayerController playerController;
    private TransactionRepository transactionRepository;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final UUID playerUUID = player.getUniqueId();

        final Document document = playerRepository.get(playerUUID);
        final PlayerData playerData;
        if (document == null) {
            playerData = new PlayerData(playerUUID);
        } else {
            playerData = new PlayerData(UUID.fromString(document.getString("uuid")), document.getDouble("balance"));
        }

        final Collection<Document> documents = transactionRepository.get(playerUUID);
        if(documents != null)
            documents.forEach(transactionDoc -> {
                Transaction transaction = new Transaction(
                        UUID.fromString(transactionDoc.getString("buyer")),
                        UUID.fromString(transactionDoc.getString("seller")),
                        UUID.fromString(transactionDoc.getString("itemID")),
                        transactionDoc.getDouble("price"),
                        transactionDoc.getLong("created_at")
                );

                playerData.addTransaction(transaction);
            });

        playerController.register(playerData);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        final Player player = e.getPlayer();
        final UUID playerUUID = player.getUniqueId();
        playerController.unregister(playerUUID);
    }
}
