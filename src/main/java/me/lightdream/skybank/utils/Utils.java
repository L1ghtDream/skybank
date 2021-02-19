package me.lightdream.skybank.utils;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.enums.LoadFileType;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class Utils {

    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }


    public static void createConfig(String path, LoadFileType type){
        try {
            if(type == LoadFileType.DEFAULT)
                FileUtils.copyInputStreamToFile(SkyBank.INSTANCE.getResource(path), new File(SkyBank.INSTANCE.getDataFolder(), path));
            else if(type == LoadFileType.PLAYER_DATA)
                FileUtils.copyInputStreamToFile(SkyBank.INSTANCE.getResource("playerData.yml"), new File(SkyBank.INSTANCE.getDataFolder(), path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig(FileConfiguration config, String path){

        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("SkyBank").getDataFolder(), path);
        if (!file.exists()) {
            try {
                file.createNewFile();
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static FileConfiguration loadConfig(String path, LoadFileType type) {

        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("SkyBank").getDataFolder(), path);
        if (file.exists()) {
            return YamlConfiguration.loadConfiguration(file);
        } else {
            createConfig(path, type);
            return loadConfig(path, type);
        }
    }

    public static void sendPrefixedMessage(CommandSender sender, String text){
        //TODO: Add the prefix
        sender.sendMessage(color(text));
    }

    public static void sendColoredMessage(CommandSender sender, String text){
        sender.sendMessage(color(text));
    }
}
