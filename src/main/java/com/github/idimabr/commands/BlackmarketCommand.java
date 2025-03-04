package com.github.idimabr.commands;

import com.github.idimabr.menus.BlackMarketMenu;
import com.github.idimabr.menus.MarketMenu;
import com.github.idimabr.utils.ConfigUtil;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class BlackmarketCommand implements CommandExecutor {

    private final ConfigUtil config;
    private final ViewFrame view;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(config.getString("messages.only-players"));
            return false;
        }

        if(!sender.hasPermission("marketplace.blackmarket")){
            sender.sendMessage(config.getString("messages.no-permission"));
            return false;
        }

        final Player player = (Player) sender;
        view.open(BlackMarketMenu.class, player);
        return false;
    }
}
