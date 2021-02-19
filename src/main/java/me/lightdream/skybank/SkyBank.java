package me.lightdream.skybank;

import me.lightdream.skybank.commands.CommandHandler;
import me.lightdream.skybank.enums.LoadFileType;
import me.lightdream.skybank.utils.Utils;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.addons.Addon;
import world.bentobox.bentobox.api.addons.GameModeAddon;

import java.util.Optional;
import java.util.logging.Logger;

public final class SkyBank extends JavaPlugin {

    public static SkyBank INSTANCE;
    public static Logger logger;
    public static FileConfiguration config;
    public static FileConfiguration data;
    public static GameModeAddon skyblock;
    public static BentoBox bentoBox;
    public static CommandHandler commandHandler;

    public static Economy economy = null;
    public static Permission perms = null;
    public static Chat chat = null;



    @Override
    public void onEnable() {

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
        //setupChat();

        //Setup Config
        config = Utils.loadConfig("config.yml", LoadFileType.DEFAULT);
        data = Utils.loadConfig("data.yml", LoadFileType.DEFAULT);

        //Setting variable of external classes
        Runnable.setLastTaxTime(config.getInt("tax-time"));
        Runnable.setTaxes(data.getInt("taxes"));

        //Command setup
        commandHandler = new CommandHandler(this);


    }

    @Override
    public void onDisable() {
        Utils.saveConfig(config, "config.yml");
        Utils.saveConfig(data, "data.yml");
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
