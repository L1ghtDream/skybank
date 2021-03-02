package me.lightdream.skybank.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.enums.LoadFileType;
import me.lightdream.skybank.enums.TaxType;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static me.lightdream.skybank.SkyBank.*;

public class API {

    //TODO: Refactor for API and switch to use UUID for all functions


    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static ArrayList<String> color(ArrayList<String> str) {

        ArrayList<String> output = new ArrayList<>();

        for(String var1 : str){
            output.add(ChatColor.translateAlternateColorCodes('&', var1));
        }

        return output;
    }

    public static void createFile(String path, LoadFileType type) {

        try {
            if(type == LoadFileType.DEFAULT)
                FileUtils.copyInputStreamToFile(SkyBank.INSTANCE.getResource(path), new File(SkyBank.INSTANCE.getDataFolder(), path));
            else if(type == LoadFileType.PLAYER_DATA){
                FileUtils.copyInputStreamToFile(SkyBank.INSTANCE.getResource("playerData.yml"), new File(SkyBank.INSTANCE.getDataFolder(), path));
                FileConfiguration data = loadFile(path, type);
                data.set("tax", SkyBank.data.getInt("tax"));
                data.set("interest", SkyBank.data.getInt("interest"));
                data.set("loan", SkyBank.data.getInt("loan"));
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

    public static Boolean checkPlayerFileExistance(String path) {
        return new File("PlayerData/" + Bukkit.getServer().getPluginManager().getPlugin("SkyBank").getDataFolder(), path).exists();
    }

    public static void sendColoredMessage(CommandSender sender, String text){

        ArrayList<String> texts = new ArrayList<>();
        for(String var1 : text.split("%newline%"))
            sender.sendMessage(color(var1));
    }

    public static int getTaxData(UUID uuid){
        return  SkyBank.data.getInt("tax") - loadPlayerDataFile(uuid).getInt("tax");
    }

    public static int getIslandSize(UUID uuid){
        Island island = getIsland(uuid);

        if (island == null) {
            SkyBank.logger.severe("No island found");
            return 0;
        }

        return island.getProtectionRange() * 2;
    }

    public static double getTaxValue(UUID uuid){

        int size = getIslandSize(uuid);

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

    public static double getTaxPrice(UUID uuid){
        return getTaxValue(uuid) * getBalance(uuid) / 100;
    }

    public static FileConfiguration loadPlayerDataFile(UUID uuid){
        return API.loadFile("PlayerData/" + uuid + ".yml", LoadFileType.PLAYER_DATA);
    }

    public static void savePlayerDataFile(UUID uuid, FileConfiguration data){
        API.saveFile(data, "PlayerData/" + uuid + ".yml");
    }

    public static double getBalance(UUID uuid){
        return SkyBank.economy.getBalance(Bukkit.getOfflinePlayer(uuid));
    }

    public static void removeBalance(UUID uuid, double balance){
        SkyBank.economy.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), balance);
    }

    public static void setIslandSize(Island island, int size){
        island.setProtectionRange(size/2);
    }

    public static Island getIsland(UUID uuid){
        return bentoBox.getIslands().getIsland(skyblock.getOverWorld(), User.getInstance(uuid));
    }

    public static void setTax(UUID uuid, TaxType type){
        if(type == TaxType.PAID){
            FileConfiguration data = loadPlayerDataFile(uuid);
            data.set("tax", SkyBank.data.getInt("tax"));
            savePlayerDataFile(uuid, data);
        }
    }

    public static void sendCommands(CommandSender sender) {
        API.sendColoredMessage(sender, "&4*&c&m                         &7*( &3&lSky Bank &7)*&c&m                          &4*");
        SkyBank.commandHandler.getCommands().forEach(command -> {
            if (command.hasPermission(sender)) {
                API.sendColoredMessage(sender, "  &7&l- " + command.sendHelpLine());
            }
        });
        API.sendColoredMessage(sender, "&4*&c&m                                                                             &4*");
    }

    public static double getInterestPercent(UUID uuid){

        double balance = getBalance(uuid);

        List<Map<?, ?>> list = SkyBank.config.getMapList("interests");
        for(Map<?, ?> map : list){
            if(balance >= Double.parseDouble(String.valueOf(map.get("min-money"))) && balance <= Double.parseDouble(String.valueOf(map.get("max-money"))) && Bukkit.getPlayer(uuid).hasPermission((String) map.get("permission"))){
                return Double.parseDouble(String.valueOf(map.get("interest")));
            }
        }
        return 0;
    }

    public static double getInterestPercent(UUID uuid, List<Map<?, ?>> list){

        double balance = getBalance(uuid);

        for(Map<?, ?> map : list){
            if(balance >= Double.parseDouble(String.valueOf(map.get("min-money"))) && balance <= Double.parseDouble(String.valueOf(map.get("max-money"))) && Bukkit.getPlayer(uuid).hasPermission((String) map.get("permission"))){
                return Double.parseDouble(String.valueOf(map.get("interest")));
            }
        }
        return 0;
    }

    public static double getInterest(UUID uuid){
        return getInterestPercent(uuid) * getBalance(uuid) / 100;
    }

    public static double getInterest(UUID uuid, double interestPercent){
        return interestPercent * getBankBalance(uuid) / 100;
    }

    public static double getBankBalance(UUID uuid){
        return loadPlayerDataFile(uuid).getDouble("bank-balance");
    }

    public static void addBankBalance(UUID uuid, double balance){
        FileConfiguration data = API.loadPlayerDataFile(uuid);
        data.set("bank-balance", data.getInt("bank-balance") + balance);
        API.savePlayerDataFile(uuid, data);
    }

    public static void removeBankBalance(UUID uuid, double balance){
        FileConfiguration data = API.loadPlayerDataFile(uuid);
        data.set("bank-balance", data.getInt("bank-balance") - balance);
        API.savePlayerDataFile(uuid, data);
    }

    public static Map<?, ?> getBankLoan(String name){

        List<Map<?, ?>> list = SkyBank.config.getMapList("loans");
        for(Map<?, ?> map : list){
            if(name.equalsIgnoreCase((String) map.get("name"))){
                return map;
            }
        }
        return null;
    }

    public static double getHoursPlayed(UUID uuid){
        return Bukkit.getOfflinePlayer(uuid).getStatistic(Statistic.PLAY_ONE_MINUTE) / 60.0;
    }

    public static ArrayList<String> getLoans(){
        ArrayList<String> output = new ArrayList<>();

        List<Map<?, ?>> list = SkyBank.config.getMapList("loans");
        for(Map<?, ?> map : list){
            output.add((String) map.get("name"));
        }
        return output;
    }

    public static boolean getLoanStatus(UUID uuid, String loan){

        if (!loanedPLayersNames.contains(uuid.toString()))
            return true;

        return !((List<String>) loanedPLayers.get(loanedPLayersNames.indexOf(uuid.toString())).get("loan")).contains(loan);
    }

    public static String getBeautifiedLoanStatus(UUID uuid, String loan){
        //System.out.println(getLoanStatus(player, loan));
        if (getLoanStatus(uuid, loan))
            return Language.loan_available;
        else
            return Language.loan_unavailable;
    }

    public static Map<?, ?> getIpLogTemplate(UUID uuid){

        Player player = Bukkit.getPlayer(uuid);

        Map<String, String> map = new HashMap<>();

        map.put("uuid", uuid.toString());
        map.put("name", player.getName());
        map.put("ip", player.getAddress().getHostName());

        return map;
    }

    public static Map<?, ?> getBankLoanTemplate(UUID uuid, double toPay, List<String> loans){
        Map<String, List<String>> map = new HashMap<>();

        map.put("uuid", Collections.singletonList(uuid.toString()));
        map.put("name", Collections.singletonList(Bukkit.getOfflinePlayer(uuid).getName()));
        map.put("to-pay", Collections.singletonList(String.valueOf(toPay)));
        map.put("loan", loans);

        return map;
    }

    public static Map<?, ?> getBankLoanTemplate(UUID uuid, double toPay, String loan, Map<?, ?> oldTemplate){
        Map<String, List<String>> map = new HashMap<>();
        List<String> loans = new ArrayList<>((List<String>) oldTemplate.get("loan"));
        loans.add(loan);

        toPay += toPay * config.getDouble("loan-interest-on-take") / 100;

        map.put("uuid", Collections.singletonList(uuid.toString()));
        map.put("name", Collections.singletonList(Bukkit.getOfflinePlayer(uuid).getName()));
        map.put("to-pay", Collections.singletonList(String.valueOf(toPay + Double.parseDouble(((List<String>) oldTemplate.get("to-pay")).get(0)))));
        map.put("loan", loans);

        return map;
    }

    public static double getAmountToPayForLoans(UUID uuid){
        int index = loanedPLayersNames.indexOf(uuid.toString());

        if(index == -1)
            return 0;

        return Double.parseDouble(((List<String>) loanedPLayers.get(index).get("to-pay")).get(0));
    }

    public static void addLoanByInterest(UUID uuid){
        Map<String, List<String>> map = (Map<String, List<String>>) API.getBankLoan(Bukkit.getOfflinePlayer(uuid).getName());
        map.put("to-pay", Collections.singletonList(String.valueOf(getAmountToPayForLoans(uuid) + getAmountToPayForLoans(uuid) * getInterestPercent(uuid) / 100)));
        setBankLoan(uuid, map);
    }

    public static void removeLoan(UUID uuid, double amount){
        Map<String, List<String>> map = (Map<String, List<String>>) API.getBankLoan(uuid);
        map.put("to-pay", Collections.singletonList(String.valueOf(getAmountToPayForLoans(uuid) - amount)));
        setBankLoan(uuid, map);
    }

    public static Map<?, ?> getBankLoan(UUID uuid){
        int index = loanedPLayersNames.indexOf(uuid.toString());

        if(index == -1)
            return null;

        return loanedPLayers.get(index);
    }

    public static void setBankLoan(UUID uuid, Map<?, ?> map){
        if(!loanedPLayersNames.contains(uuid.toString())){
            loanedPLayersNames.add(uuid.toString());
            loanedPLayers.add(map);
        }
        else{
            int index =  loanedPLayersNames.indexOf(uuid.toString());
            loanedPLayers.set(index, map);
        }

    }

    public static double getPlayerOvertaxValue(UUID uuid){
        if(getTaxData(uuid) >= SkyBank.config.getInt("tax-limit")){
            double output = SkyBank.config.getDouble("over-tax");

            if(API.getIsland(uuid).getOwner() == uuid)
                return 0;

            return output;
        }
        return 0;
    }

    public static double getPlayerOvertaxPrice(UUID uuid){
        if(getTaxData(uuid) >= SkyBank.config.getInt("tax-limit")){
            double output = getTaxPrice(uuid) * getTaxData(uuid) * getPlayerOvertaxValue(uuid) / 100;

            if(API.getIsland(uuid).getOwner() == uuid)
                return 0;

            return output;
        }
        return 0;
    }

    public static int getLoanData(UUID uuid) {
        return  SkyBank.data.getInt("loan") - loadPlayerDataFile(uuid).getInt("loan");
    }

    public static String processPlaceholder1(UUID uuid, String s1, String s2){
        return color(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(uuid), process1(s1, s2)));
    }

    public static ArrayList<String> processPlaceholder2(UUID uuid, String s1, String s2){
        return color(new ArrayList<>(PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(uuid), process2(s1, s2))));
    }

    public static String process1(String s1, String s2){
        return SkyBank.guiConfig.getString(String.format(s1, s2));
    }

    public static List<String> process2 (String s1, String s2){
        return (List<String>) SkyBank.guiConfig.getList(String.format(s1, s2));
    }

    public static boolean getGUIStatus(UUID uuid){
        return loadPlayerDataFile(uuid).getBoolean("gui");
    }

    public static double getTaxTotalPrice(UUID uuid){
        return getPlayerOvertaxPrice(uuid) + getTaxPrice(uuid) * getTaxData(uuid);
    }

}
