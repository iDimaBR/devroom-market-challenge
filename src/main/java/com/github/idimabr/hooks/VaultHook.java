package com.github.idimabr.hooks;

import com.github.idimabr.DevRoomMarket;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {
    public static Economy eco = null;

    public boolean setupEconomy(DevRoomMarket plugin) {
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("Vault")) {
            RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
            if (economyProvider != null) {
                eco = economyProvider.getProvider();
                plugin.getLogger().info("Vault foi registrado!");
            }
            return (eco != null);
        } else {
            plugin.getLogger().warning("Vault não encontrado, desabilitando plugin.");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return false;
        }
    }

    public static Economy getEconomy() {
        return eco;
    }
}
