package me.lightdream.skybank;

import me.lightdream.skybank.commands.CommandHandler;
import me.lightdream.skybank.dependencies.PAPI;
import me.lightdream.skybank.enums.LoadFileType;
import me.lightdream.skybank.listener.EventListener;
import me.lightdream.skybank.utils.Language;
import me.lightdream.skybank.utils.API;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.GameModeAddon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

public final class SkyBank extends JavaPlugin {

    public static SkyBank INSTANCE;
    public static Logger logger;
    public static FileConfiguration config;
    public static FileConfiguration data;
    public static FileConfiguration ipLog;
    public static FileConfiguration guiConfig;
    public static FileConfiguration actionLogger;
    public static GameModeAddon skyblock;
    public static BentoBox bentoBox;
    public static CommandHandler commandHandler;

    public static Economy economy = null;
    public static Permission perms = null;
    public static Chat chat = null;


    public static ArrayList<String> overtaxedPlayersNames = new ArrayList<>();
    public static List<Map<?, ?>> loanedPLayers;
    public static ArrayList<String> loanedPLayersNames = new ArrayList<>();
    public static List<Map<?, ?>> playerLogger;
    public static ArrayList<String> playerLoggerNames = new ArrayList<>();
    public static List<String> actionLoggerList = new ArrayList<>();


    public static NamespacedKey payTax;
    public static NamespacedKey taxGUI;
    public static NamespacedKey loanGUI;
    public static NamespacedKey bankGUI;
    public static NamespacedKey deposit;
    public static NamespacedKey withdraw;
    public static NamespacedKey loan;

    @Override
    public void onEnable() {

        payTax = new NamespacedKey(this,"payTax");
        taxGUI = new NamespacedKey(this,"taxGUI");
        loanGUI = new NamespacedKey(this,"loanGUI");
        bankGUI = new NamespacedKey(this,"bankGUI");
        deposit = new NamespacedKey(this,"deposit");
        withdraw = new NamespacedKey(this,"withdraw");
        loan = new NamespacedKey(this,"loan");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PAPI().register();
        } else {
            System.out.println("Could not find PlaceholderAPI! This plugin is required.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        //Setup instancing
        logger = super.getLogger();
        bentoBox = BentoBox.getInstance();
        INSTANCE = this;

        //Setup BSkyBlock
        Optional<Addon> addon = bentoBox.getAddonsManager().getAddonByName("BSkyBlock");
        if(!addon.isPresent()){
            logger.severe("BSkyBlock Addon has not been found");
        }
        skyblock = (GameModeAddon) addon.get();

        if (!setupEconomy()) {
            logger.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupPermissions();
        Language.loadLang();

        //Setup data files
        config = API.loadFile("config.yml", LoadFileType.DEFAULT);
        data = API.loadFile("data.yml", LoadFileType.DEFAULT);
        ipLog = API.loadFile("ip.yml", LoadFileType.DEFAULT);
        guiConfig = API.loadFile("gui.yml", LoadFileType.DEFAULT);
        actionLogger = API.loadFile("log.yml", LoadFileType.DEFAULT);
        loanedPLayers = API.loadFile("data.yml", LoadFileType.DEFAULT).getMapList("loaned-players");

        for(Map<?, ?> map : loanedPLayers)
            loanedPLayersNames.add((String) ((List<String>) map.get("uuid")).get(0));

        //Setup ip.yml
        playerLogger = ipLog.getMapList("log");

        actionLoggerList = (List<String>) actionLogger.getList("log");

        API.checkActionLoggerSize();


        for(Map<?, ?> map : playerLogger)
            playerLoggerNames.add((String) map.get("uuid"));

        //Setting variable of external classes
        Runnable.taxTime = config.getInt("tax-time") * 60 * 20;
        Runnable.interestTime = config.getInt("interest-time") * 60 * 20;
        Runnable.loanTime = config.getInt("loanTime-time") * 60 * 20;

        //Command setup
        commandHandler = new CommandHandler(this);
        getServer().getPluginManager().registerEvents(new EventListener(), this);

        Runnable.init();

    }

    @Override
    public void onDisable() {
        data.set("loaned-players", loanedPLayers);

        API.saveFile(config, "config.yml");
        API.saveFile(data, "data.yml");
        API.saveFile(ipLog, "ip.yml");
    }

    public static void reload(){
        data.set("loaned-players", loanedPLayers);

        API.saveFile(config, "config.yml");
        API.saveFile(data, "data.yml");
        API.saveFile(ipLog, "ip.yml");

        config = API.loadFile("config.yml", LoadFileType.DEFAULT);
        data = API.loadFile("data.yml", LoadFileType.DEFAULT);
        ipLog = API.loadFile("ip.yml", LoadFileType.DEFAULT);
        guiConfig = API.loadFile("gui.yml", LoadFileType.DEFAULT);
        actionLogger = API.loadFile("log.yml", LoadFileType.DEFAULT);
        loanedPLayers = API.loadFile("data.yml", LoadFileType.DEFAULT).getMapList("loaned-players");

        for(Map<?, ?> map : loanedPLayers)
            loanedPLayersNames.add((String) ((List<String>) map.get("uuid")).get(0));

        playerLogger = ipLog.getMapList("log");
        actionLoggerList = (List<String>) actionLogger.getList("log");

        API.checkActionLoggerSize();

        for(Map<?, ?> map : playerLogger)
            playerLoggerNames.add((String) map.get("uuid"));

        Runnable.taxTime = config.getInt("tax-time") * 60 * 20;
        Runnable.interestTime = config.getInt("interest-time") * 60 * 20;
        Runnable.loanTime = config.getInt("loanTime-time") * 60 * 20;

    }


    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
}
