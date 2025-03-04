package com.github.idimabr.menus;

import com.github.idimabr.DevRoomMarket;
import com.github.idimabr.controller.MarketController;
import com.github.idimabr.controller.PlayerController;
import com.github.idimabr.models.MarketData;
import com.github.idimabr.models.PlayerData;
import com.github.idimabr.models.Transaction;
import com.github.idimabr.utils.ConfigUtil;
import com.github.idimabr.utils.ItemBuilder;
import com.github.idimabr.utils.ItemSerializer;
import me.saiintbrisson.minecraft.PaginatedView;
import me.saiintbrisson.minecraft.PaginatedViewSlotContext;
import me.saiintbrisson.minecraft.ViewContext;
import me.saiintbrisson.minecraft.ViewItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionMenu extends PaginatedView<Transaction> {

    private PlayerController playerController;
    private ConfigUtil config;

    public TransactionMenu(DevRoomMarket plugin, int row, String title) {
        super(row, title);
        setCancelOnClick(true);
        this.config = plugin.getConfig();
        this.playerController = plugin.getPlayerController();
        setCancelOnClick(true);
        scheduleUpdate(5);
        setLayout(config.getStringList("menus.transaction.layout").stream().limit(row).toArray(String[]::new));
        setPreviousPageItemSlot(config.getInt("menu-config.back.slot", 0));
        setPreviousPageItem((ctx, item) -> {
            item.withItem(
                    new ItemBuilder(config.getString("menu-config.back.material", "ARROW"))
                            .setName(config.getString("menu-config.back.name").replace("&","§"))
                            .build()
            ).onClick($ -> {
                if (ctx.isFirstPage()){
                    return;
                }
                ctx.switchToPreviousPage();
            });
        });
        setNextPageItemSlot(config.getInt("menu-config.next.slot", 8));
        setNextPageItem((ctx, item) -> {
            if(ctx.isLastPage()) return;
            item.withItem(
                    new ItemBuilder(config.getString("menu-config.next.material", "ARROW"))
                            .setName(config.getString("menu-config.next.name").replace("&","§"))
                            .build()
            );
        });
    }

    @Override
    protected void onRender(@NotNull ViewContext context) {
        final Player player = context.getPlayer();
        final PlayerData data = playerController.getRegistry(player.getUniqueId());
        if(data == null) {
            player.sendMessage(config.getString("messages.error").replace("&","§"));
            context.close();
            return;
        }

        context.paginated().setSource(data.getTransactions());
    }

    @Override
    protected void onItemRender(@NotNull PaginatedViewSlotContext<Transaction> context, @NotNull ViewItem viewItem, @NotNull Transaction data) {
        final ConfigurationSection item = config.getConfigurationSection("menus.transaction.item-format");

        if(item == null) return;
        if(!item.isSet("material")) return;

        final String materialName = item.getString("material");
        final ItemBuilder builder = new ItemBuilder(materialName);

        if(materialName.equalsIgnoreCase("PLAYER_HEAD"))
            builder.setSkullOwner(data.getBuyerName());

        if (item.isSet("name"))
            builder.setName(
                    item.getString("name")
                            .replace("{price}", data.getPrice()+"")
                            .replace("{seller}", data.getSellerName())
                            .replace("{buyer}", data.getBuyerName())
                            .replace("{date}", data.getListed())
                            .replace("&", "§")
            );

        if (item.isSet("data"))
            builder.setDurability((short) item.getInt("data"));

        if (item.isSet("lore"))
            builder.setLore(item.getStringList("lore").stream()
                    .map($ -> $.replace("&", "§")
                            .replace("{price}", data.getPrice()+"")
                            .replace("{seller}", data.getSellerName())
                            .replace("{buyer}", data.getBuyerName())
                            .replace("{date}", data.getListed())
                    ).collect(Collectors.toList()));

        viewItem.withItem(builder.build());
    }
}
