package com.github.idimabr.commands;

import com.github.idimabr.controller.MarketController;
import com.github.idimabr.database.repository.impl.MarketplaceRepository;
import com.github.idimabr.models.MarketData;
import com.github.idimabr.utils.ConfigUtil;
import com.github.idimabr.utils.ItemSerializer;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@AllArgsConstructor
public class SellCommand implements CommandExecutor {

    private final ConfigUtil config;
    private final MarketplaceRepository marketRepository;
    private final MarketController marketController;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(config.getString("messages.only-players"));
            return false;
        }

        if(!sender.hasPermission("marketplace.sell")){
            sender.sendMessage(config.getString("messages.no-permission"));
            return false;
        }

        final Player player = (Player) sender;
        if(args.length == 0) {
            player.sendMessage(config.getString("messages.sell-usage"));
            return false;
        }

        final String priceArgument = args[0];
        if(!priceArgument.matches("[0-9]+")) {
            player.sendMessage(config.getString("messages.invalid-price"));
            return false;
        }

        final double price = Double.parseDouble(priceArgument);
        if(price <= 0) {
            player.sendMessage(config.getString("messages.invalid-price"));
            return false;
        }

        final PlayerInventory inventory = player.getInventory();
        final ItemStack item = inventory.getItemInMainHand();
        if(item == null || item.getType() == Material.AIR) {
            player.sendMessage(config.getString("messages.hold-item"));
            return false;
        }

        final UUID itemID = UUID.randomUUID();
        final String base64item = ItemSerializer.write(item);

        marketController.register(new MarketData(
                itemID,
                player.getUniqueId(),
                base64item,
                price,
                System.currentTimeMillis()
        ));
        marketRepository.create(itemID, player.getUniqueId(), base64item, price);
        inventory.setItemInMainHand(null);
        player.sendMessage(config.getString("messages.sell-item").replace("{price}", String.valueOf(price)));
        return false;
    }
}
