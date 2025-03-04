package com.github.idimabr.models;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class PlayerData {

    private UUID uuid;
    private final String name;
    private double balance;
    private List<Transaction> transactions;

    public PlayerData(UUID uuid, double balance) {
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(uuid).getName();
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
        this.name = Bukkit.getOfflinePlayer(uuid).getName();
        this.balance = 0;
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }
}
