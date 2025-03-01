package com.github.idimabr.controller;

import com.github.idimabr.models.PlayerData;
import java.util.*;

public class PlayerController {

    private Map<UUID, PlayerData> players = new HashMap<>();

    public void register(PlayerData playerData) {
        players.put(playerData.getUuid(), playerData);
    }

    public void unregister(UUID uuid) {
        players.remove(uuid);
    }

    public PlayerData getRegistry(UUID uuid) {
        return players.get(uuid);
    }

    public Map<UUID, PlayerData> getPlayers() {
        return players;
    }

    public boolean isRegistered(UUID uuid) {
        return players.containsKey(uuid);
    }
}
