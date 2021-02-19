package me.lightdream.skybank.utils;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.enums.LoadFileType;
import me.lightdream.skybank.enums.TaxType;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;

import java.io.*;
import java.util.List;
import java.util.UUID;

import static me.lightdream.skybank.SkyBank.bentoBox;
import static me.lightdream.skybank.SkyBank.skyblock;

public class Utils {

    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static void createFile(String path, LoadFileType type){

        try {
            if(type == LoadFileType.DEFAULT)
                FileUtils.copyInputStreamToFile(SkyBank.INSTANCE.getResource(path), new File(SkyBank.INSTANCE.getDataFolder(), path));
            else if(type == LoadFileType.PLAYER_DATA){
                FileUtils.copyInputStreamToFile(SkyBank.INSTANCE.getResource("playerData.yml"), new File(SkyBank.INSTANCE.getDataFolder(), path));
                FileConfiguration data = loadFile(path, type);
                data.set("tax", SkyBank.data.getInt("tax"));
                saveFile(data, path);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(FileConfiguration config, String path){

        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("SkyBank").getDataFolder(), path);
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static FileConfiguration loadFile(String path, LoadFileType type) {

        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("SkyBank").getDataFolder(), path);
        if (file.exists()) {
            return YamlConfiguration.loadConfiguration(file);
        } else {
            createFile(path, type);
            return loadFile(path, type);
        }
    }

    public static void sendPrefixedMessage(CommandSender sender, String text){
        //TODO: Add the prefix
        sender.sendMessage(color(text));
    }

    public static void sendColoredMessage(CommandSender sender, String text){
        sender.sendMessage(color(text));
    }

    public static int getTax(Player player){
        return Math.abs(SkyBank.data.getInt("tax") - getPlayerDataFile(player).getInt("tax"));
    }

    public static int getTax(UUID uuid){
        return  SkyBank.data.getInt("tax") - getPlayerDataFile(uuid).getInt("tax");
    }

    public static int getIslandSize(Player player){
        Island island = getIsland(player);

        if (island == null) {
            SkyBank.logger.severe("No island found");
            Utils.sendPrefixedMessage(player, Language.no_island_found);
            return 0;
        }

        return island.getProtectionRange() * 2;
    }

    public static double getTaxValue(Player player){

        int size = getIslandSize(player);

        List<String> taxes = (List<String>) SkyBank.config.getList("border-tax");

        for(String var1 : taxes){
            String[] var2 = var1.split(":");
            if(Integer.parseInt(var2[0]) == size){
                return Double.parseDouble(var2[1]);
            }
            else if (Integer.parseInt(var2[0]) >= size){
                break;
            }
        }
        return Double.parseDouble(taxes.get(0).split(":")[1]);
    }

    public static double getTaxValue(Player player, int size){

        List<String> taxes = (List<String>) SkyBank.config.getList("border-tax");

        for(String var1 : taxes){
            String[] var2 = var1.split(":");
            if(Integer.parseInt(var2[0]) == size){
                return Double.parseDouble(var2[1]);
            }
            else if (Integer.parseInt(var2[0]) >= size){
                break;
            }
        }
        return Double.parseDouble(taxes.get(0).split(":")[1]);
    }

    public static double getTaxPrice(Player player){
        return getTaxValue(player) * getBalance(player) / 100;
    }

    public static double getTaxPrice(Player player, double taxValue){
        return taxValue * getBalance(player) / 100;
    }

    public static FileConfiguration getPlayerDataFile(Player player){
        return Utils.loadFile("PlayerData/" + (player).getUniqueId() + ".yml", LoadFileType.PLAYER_DATA);
    }

    public static FileConfiguration getPlayerDataFile(UUID uuid){
        return Utils.loadFile("PlayerData/" + uuid + ".yml", LoadFileType.PLAYER_DATA);
    }

    public static void savePlayerDataFile(Player player, FileConfiguration data){
        Utils.saveFile(data, "PlayerData/" + (player).getUniqueId() + ".yml");
    }

    public static double getBalance(Player player){
        return SkyBank.economy.getBalance(player);
    }

    public static void removeBalance(Player player, double balance){
        SkyBank.economy.withdrawPlayer(player, balance);
    }

    public static void addBalance(Player player, double balance){
        SkyBank.economy.depositPlayer(player, balance);
    }
    
    public static void setIslandSize(Player player, int size){
        getIsland(player).setProtectionRange(size/2);
    }

    public static void setIslandSize(Island island, int size){
        island.setProtectionRange(size/2);
    }

    public static Island getIsland(Player player){
        return bentoBox.getIslands().getIsland(skyblock.getOverWorld(), User.getInstance(player));
    }

    public static void setTax(Player player, TaxType type){
        if(type == TaxType.PAID){
            FileConfiguration data = getPlayerDataFile(player);
            data.set("tax", SkyBank.data.getInt("tax"));
            savePlayerDataFile(player, data);
        }
    }

    public static void sendCommands(CommandSender sender) {
        Utils.sendColoredMessage(sender, "&4*&c&m                         &7*( &3&lHungerGames &7)*&c&m                          &4*");
        SkyBank.commandHandler.getCommands().forEach(command -> {
            if (command.hasPermission(sender)) {
                Utils.sendColoredMessage(sender, "  &7&l- " + command.sendHelpLine());
            }
        });
        Utils.sendColoredMessage(sender, "&4*&c&m                                                                             &4*");
    }

}
