package com.github.idimabr.commands;

import com.github.idimabr.menus.BlackMarketMenu;
import com.github.idimabr.menus.TransactionMenu;
import lombok.AllArgsConstructor;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
public class TransactionsCommand implements CommandExecutor {

    private final ViewFrame view;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players!.");
            return false;
        }

        if(!sender.hasPermission("marketplace.history")){
            sender.sendMessage("§cYou do not have permission to use this command.");
            return false;
        }

        final Player player = (Player) sender;
        view.open(TransactionMenu.class, player);
        return false;
    }
}
