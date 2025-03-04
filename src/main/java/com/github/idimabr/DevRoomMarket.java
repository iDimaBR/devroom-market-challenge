package com.github.idimabr;

import com.github.idimabr.commands.BlackmarketCommand;
import com.github.idimabr.commands.MarketplaceCommand;
import com.github.idimabr.commands.SellCommand;
import com.github.idimabr.commands.TransactionsCommand;
import com.github.idimabr.controller.MarketController;
import com.github.idimabr.controller.PlayerController;
import com.github.idimabr.database.connection.MongoDBConnection;
import com.github.idimabr.database.repository.impl.MarketplaceRepository;
import com.github.idimabr.database.repository.impl.PlayerRepository;
import com.github.idimabr.database.repository.impl.TransactionRepository;
import com.github.idimabr.hooks.DiscordHook;
import com.github.idimabr.hooks.VaultHook;
import com.github.idimabr.listeners.JoinListener;
import com.github.idimabr.menus.BlackMarketMenu;
import com.github.idimabr.menus.ConfirmMenu;
import com.github.idimabr.menus.MarketMenu;
import com.github.idimabr.menus.TransactionMenu;
import com.github.idimabr.tasks.BlackMarketTask;
import com.github.idimabr.utils.ConfigUtil;
import com.google.gson.Gson;
import lombok.Getter;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Logger;

@Getter
public final class DevRoomMarket extends JavaPlugin {

    private ConfigUtil config;
    private MongoDBConnection database;
    private PlayerRepository playerRepository;

    private MarketController marketController;
    private PlayerController playerController;

    private TransactionRepository transactionRepository;
    private MarketplaceRepository marketRepository;
    private VaultHook vaultHook;
    private DiscordHook discordHook;
    private ViewFrame view;

    public static Gson GSON = new Gson();
    public static Logger LOGGER;

    @Override
    public void onLoad() {
        this.config = new ConfigUtil(this, "config.yml");
    }

    @Override
    public void onEnable() {
        LOGGER = getLogger();
        startEconomy();
        loadDatabase();
        loadControllers();
        loadHooks();
        loadMenu();
        loadCommands();
        loadListeners();
        loadTasks();
    }

    @Override
    public void onDisable() {
        unloadDatabase();
    }

    private void loadCommands(){
        getCommand("marketplace").setExecutor(new MarketplaceCommand(config, view));
        getCommand("sell").setExecutor(new SellCommand(config, marketRepository, marketController));
        getCommand("blackmarket").setExecutor(new BlackmarketCommand(config, view));
        getCommand("transactions").setExecutor(new TransactionsCommand(view, config));
    }

    private void loadListeners(){
        final PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new JoinListener(playerRepository, playerController, transactionRepository), this);
    }

    private void loadDatabase() {
        database = new MongoDBConnection(this);
        playerRepository = new PlayerRepository(database);
        transactionRepository = new TransactionRepository(database);
        marketRepository = new MarketplaceRepository(database);
    }

    private void loadControllers(){
        this.marketController = new MarketController();
        this.marketController.load(marketRepository.getAll());
        this.playerController = new PlayerController();
    }

    private void loadHooks(){
        if(config.getBoolean("discord.enabled"))
            discordHook = new DiscordHook(this);
    }

    private void loadMenu(){
        view = ViewFrame.of(this,
                new MarketMenu(this,
                        config.getInt("menus.market.row", 5),
                        config.getString("menus.market.title", "Market")
                ),
                new ConfirmMenu(this,
                        config.getInt("menus.confirmation.row", 3),
                        config.getString("menus.confirmation.title", "Confirm")
                ),
                new TransactionMenu(this,
                        config.getInt("menus.transaction.row", 5),
                        config.getString("menus.transaction.title", "Transactions")
                ),
                new BlackMarketMenu(this,
                        config.getInt("menus.blackmarket.row", 5),
                        config.getString("menus.blackmarket.title", "Black Market")
                )
        );
        view.register();
    }

    private void loadTasks(){
        new BlackMarketTask(marketController).runTaskTimer(this, 0, 20 * 60 * config.getInt("config.update-blackmarket", 5));
    }

    private void startEconomy() {
        this.vaultHook = new VaultHook();
        this.vaultHook.setupEconomy(this);
    }

    private void unloadDatabase(){
        if(database != null)
            database.disconnect();
    }

}
