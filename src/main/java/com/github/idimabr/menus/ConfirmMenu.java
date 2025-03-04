package com.github.idimabr.menus;

import com.github.idimabr.DevRoomMarket;
import com.github.idimabr.controller.MarketController;
import com.github.idimabr.controller.PlayerController;
import com.github.idimabr.database.repository.impl.MarketplaceRepository;
import com.github.idimabr.database.repository.impl.TransactionRepository;
import com.github.idimabr.hooks.DiscordHook;
import com.github.idimabr.hooks.VaultHook;
import com.github.idimabr.models.MarketData;
import com.github.idimabr.models.PlayerData;
import com.github.idimabr.models.Transaction;
import com.github.idimabr.utils.ConfigUtil;
import com.github.idimabr.utils.ItemBuilder;
import me.saiintbrisson.minecraft.View;
import me.saiintbrisson.minecraft.ViewContext;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import java.util.stream.Collectors;

public class ConfirmMenu extends View {

    private final PlayerController playerController;
    private final MarketController marketController;
    private final MarketplaceRepository marketplaceRepository;
    private final TransactionRepository transactionRepository;
    private final ConfigUtil config;
    private final DiscordHook discordHook;

    public ConfirmMenu(DevRoomMarket plugin, int row, String title) {
        super(row, title);
        setCancelOnClick(true);
        this.playerController = plugin.getPlayerController();
        this.marketController = plugin.getMarketController();
        this.marketplaceRepository = plugin.getMarketRepository();
        this.transactionRepository = plugin.getTransactionRepository();
        this.discordHook = plugin.getDiscordHook();
        this.config = plugin.getConfig();
        scheduleUpdate(5);
    }

    @Override
    public void update(@NotNull ViewContext context) {
        final Player player = context.getPlayer();
        final MarketData data = context.get("marketdata");
        final ItemStack base = context.get("item");
        final String type = context.get("type");

        final ItemMeta meta = base.getItemMeta();
        final String name = meta.hasDisplayName() ? meta.getDisplayName() : base.getType().name();

        final double originalPrice = data.getPrice();
        final double buyerPrice = type.equals("normal") ? originalPrice : originalPrice * 0.5;

        final ConfigurationSection section = config.getConfigurationSection("menus.confirmation.items");
        for (String identifier : section.getKeys(false)) {
            final ConfigurationSection item = section.getConfigurationSection(identifier);
            if (!item.isSet("material") || !item.isSet("slot")) continue;

            final ItemBuilder builder = new ItemBuilder(item.getString("material"));
            if (item.getString("material").equalsIgnoreCase("PLAYER_HEAD"))
                builder.setSkullOwner(player.getName());

            if (item.isSet("name"))
                builder.setName(item.getString("name")
                        .replace("{name}", name)
                        .replace("{player}", player.getName())
                        .replace("{buyer_price}", String.format("%.2f", buyerPrice))
                        .replace("{seller_earnings}", String.format("%.2f", originalPrice))
                        .replace("{seller}", data.getSellerName())
                        .replace("{date}", data.getListed())
                        .replace("&", "§"));

            if (item.isSet("lore"))
                builder.setLore(item.getStringList("lore").stream()
                        .map(l -> l.replace("&", "§")
                                .replace("{name}", name)
                                .replace("{player}", player.getName())
                                .replace("{buyer_price}", String.format("%.2f", buyerPrice))
                                .replace("{seller_earnings}", String.format("%.2f", originalPrice))
                                .replace("{seller}", data.getSellerName())
                                .replace("{date}", data.getListed()))
                        .collect(Collectors.toList()));

            context.slot(item.getInt("slot"), builder.build()).onClick(e -> {
                if (!identifier.equalsIgnoreCase("confirm")) return;

                final Economy economy = VaultHook.getEconomy();
                if (!economy.has(player, buyerPrice)) {
                    player.sendMessage(config.getString("messages.no-money")
                            .replace("{buyer_price}", String.format("%.2f", buyerPrice)));
                    return;
                }

                final PlayerData registry = playerController.getRegistry(player.getUniqueId());
                if (registry == null) {
                    player.sendMessage(config.getString("messages.error").replace("&", "§"));
                    return;
                }

                final long millis = System.currentTimeMillis();
                registry.addTransaction(new Transaction(
                        player.getUniqueId(),
                        data.getSellerId(),
                        data.getItemId(),
                        buyerPrice,
                        millis
                ));
                transactionRepository.create(
                        data.getSellerId(),
                        player.getUniqueId(),
                        data.getItemId(),
                        buyerPrice,
                        millis
                );
                marketController.unregister(data);
                marketplaceRepository.delete(data.getItemId());

                economy.withdrawPlayer(player, buyerPrice);
                economy.depositPlayer(data.getSellerName(), originalPrice);
                player.getInventory().addItem(base);
                player.sendMessage(config.getString("messages.buy-item")
                        .replace("{seller}", data.getSellerName())
                        .replace("{name}", name)
                        .replace("{buyer_price}", String.format("%.2f", buyerPrice)));

                final Player seller = Bukkit.getPlayer(data.getSellerId());
                if (seller != null) {
                    seller.sendMessage(config.getString("messages.buy-item-seller")
                            .replace("{buyer}", player.getName())
                            .replace("{name}", name)
                            .replace("{seller_earnings}", String.format("%.2f", originalPrice)));
                }
                discordHook.send(data, player, type);
                context.open(MarketMenu.class);
            });
        }
    }

    @Override
    public void onRender(@NotNull ViewContext context) {
        context.slot(config.getInt("menu-config.not-paginated.slot-default", 0)).withItem(
                        new ItemBuilder(config.getString("menu-config.back.material", "ARROW"))
                                .setName(config.getString("menu-config.back.name").replace("&", "§"))
                                .build())
                .onClick($ -> $.open(MarketMenu.class));

        update(context);
    }
}