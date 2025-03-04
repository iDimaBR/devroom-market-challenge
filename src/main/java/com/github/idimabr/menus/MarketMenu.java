package com.github.idimabr.menus;

import com.github.idimabr.DevRoomMarket;
import com.github.idimabr.controller.MarketController;
import com.github.idimabr.models.MarketData;
import com.github.idimabr.utils.ConfigUtil;
import com.github.idimabr.utils.ItemBuilder;
import com.github.idimabr.utils.ItemSerializer;
import me.saiintbrisson.minecraft.PaginatedView;
import me.saiintbrisson.minecraft.PaginatedViewSlotContext;
import me.saiintbrisson.minecraft.ViewContext;
import me.saiintbrisson.minecraft.ViewItem;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarketMenu extends PaginatedView<MarketData> {

    private MarketController marketController;
    private ConfigUtil config;

    public MarketMenu(DevRoomMarket plugin, int row, String title) {
        super(row, title);
        setCancelOnClick(true);
        this.config = plugin.getConfig();
        this.marketController = plugin.getMarketController();
        setCancelOnClick(true);
        scheduleUpdate(5);
        setLayout(config.getStringList("menus.market.layout").stream().limit(row).toArray(String[]::new));
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
        List<MarketData> items = marketController.getMarketData();
        context.paginated().setSource(items);
    }

    @Override
    protected void onItemRender(@NotNull PaginatedViewSlotContext<MarketData> context, @NotNull ViewItem viewItem, @NotNull MarketData data) {
        final ConfigurationSection item = config.getConfigurationSection("menus.market.item-format");

        final ItemStack base = ItemSerializer.read(data.getItemBase64());
        if(base == null) return;

        final ItemStack clone = base.clone();
        final ItemMeta meta = base.getItemMeta();

        final String name = meta.hasDisplayName() ? meta.getDisplayName() : base.getType().name();
        final String itemName = item.getString("name")
                .replace("&", "§")
                .replace("{name}", name)
                .replace("{price}", data.getPrice()+"")
                .replace("{seller}", data.getSellerName())
                .replace("{listed}", data.getListed());

        final List<String> itemLore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        item.getStringList("lore").stream()
                .map($ -> $.replace("&", "§")
                        .replace("{name}", itemName)
                        .replace("{price}", data.getPrice()+"")
                        .replace("{seller}", data.getSellerName())
                        .replace("{listed}", data.getListed())
                ).forEach(itemLore::add);

        meta.setDisplayName(itemName);
        meta.setLore(itemLore);
        base.setItemMeta(meta);

        viewItem.withItem(base).onClick(e -> {
            final Map<String, Object> mapData = new HashMap<>();
            mapData.put("marketdata", data);
            mapData.put("item", clone);
            mapData.put("type", "normal");
            e.open(ConfirmMenu.class, mapData);
        });
    }
}
