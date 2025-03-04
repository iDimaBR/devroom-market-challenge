package com.github.idimabr.hooks;

import com.github.idimabr.DevRoomMarket;
import com.github.idimabr.models.MarketData;
import com.github.idimabr.utils.ConfigUtil;
import com.github.idimabr.utils.ItemSerializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.net.ssl.HttpsURLConnection;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class DiscordHook {

    private final DevRoomMarket plugin;
    private final String webhookUrl;

    public DiscordHook(DevRoomMarket plugin) {
        this.plugin = plugin;
        this.webhookUrl = plugin.getConfig().getString("discord.webhook");
    }

    public void send(MarketData data, Player buyer) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                URL url = new URL(webhookUrl);
                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                final String formattedDate = sdf.format(data.getListedAt());

                final FileConfiguration config = plugin.getConfig();
                final ItemStack item = ItemSerializer.read(data.getItemBase64());
                final int color = toDecimal(config.getString("discord.embed.color", "#FFFFFF"));

                final List<Map<?, ?>> fieldsList = config.getMapList("discord.embed.fields");
                final JsonArray fieldsArray = new JsonArray();

                for (Map<?, ?> field : fieldsList) {
                    final JsonObject fieldObject = new JsonObject();
                    fieldObject.addProperty("name", field.get("name").toString());
                    fieldObject.addProperty(
                            "value",
                            field.get("value").toString()
                                    .replace("{item}", item.getType().name())
                                    .replace("{seller}", Bukkit.getOfflinePlayer(data.getSellerId()).getName())
                                    .replace("{buyer}", buyer.getName())
                                    .replace("{price}", "$" + data.getPrice())
                                    .replace("{date}", formattedDate)
                    );
                    fieldObject.addProperty("inline", Boolean.parseBoolean(field.get("inline").toString()));

                    fieldsArray.add(fieldObject);
                }

                JsonObject embed = new JsonObject();
                embed.addProperty("title", config.getString("discord.embed.title", "Market Alert"));
                embed.addProperty("description", String.join("\n", config.getStringList("discord.embed.description")));
                embed.addProperty("color", color);

                JsonObject footer = new JsonObject();
                footer.addProperty("text", config.getString("discord.embed.footer", "Market System"));
                embed.add("footer", footer);

                embed.add("fields", fieldsArray);

                JsonObject payload = new JsonObject();
                payload.addProperty("username", config.getString("discord.embed.username", "MarketBot"));

                JsonArray embedsArray = new JsonArray();
                embedsArray.add(embed);
                payload.add("embeds", embedsArray);

                try (OutputStream outputStream = connection.getOutputStream()) {
                    byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
                    outputStream.write(input, 0, input.length);
                }

                connection.getResponseCode();

            } catch (Exception e) {
                Bukkit.getLogger().warning("Erro ao enviar webhook: " + e.getMessage());
            }
        });
    }

    private int toDecimal(String hex) {
        if (hex == null || !hex.matches("#?[0-9A-Fa-f]{6}")) {
            return 0xFFFFFF;
        }
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        return Integer.parseInt(hex, 16);
    }
}