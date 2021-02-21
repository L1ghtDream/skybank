package me.lightdream.skybank.utils;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.enums.LoadFileType;
import me.lightdream.skybank.enums.TaxType;
import me.lightdream.skybank.exceptions.FileNotFoundException;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.AppendableOutputStream;
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

    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static void createFile(String path, LoadFileType type) throws FileNotFoundException {

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

    public static FileConfiguration loadFile(String path, LoadFileType type) throws FileNotFoundException {

        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("SkyBank").getDataFolder(), path);
        if (file.exists()) {
            return YamlConfiguration.loadConfiguration(file);
        } else {
            if(type == LoadFileType.PLAYER_DATA_READ_ONLY)
                throw new FileNotFoundException();
            else
                createFile(path, type);
            return loadFile(path, type);
        }
    }

    public static void sendColoredMessage(CommandSender sender, String text){

        ArrayList<String> texts = new ArrayList<>();
        for(String var1 : text.split("%newline%"))
            sender.sendMessage(color(var1));
    }

    public static int getTaxData(Player player) throws FileNotFoundException {
        return Math.abs(SkyBank.data.getInt("tax") - loadPlayerDataFile(player, LoadFileType.PLAYER_DATA_READ_ONLY).getInt("tax"));
    }

    public static int getTaxData(UUID uuid) throws FileNotFoundException {
        return  SkyBank.data.getInt("tax") - loadPlayerDataFile(uuid, LoadFileType.PLAYER_DATA_READ_ONLY).getInt("tax");
    }

    public static int getInterestData(Player player) throws FileNotFoundException {
        return Math.abs(SkyBank.data.getInt("interest") - loadPlayerDataFile(player, LoadFileType.PLAYER_DATA_READ_ONLY).getInt("interest"));
    }

    public static int getInterestData(UUID uuid) throws FileNotFoundException {
        return  SkyBank.data.getInt("interest") - loadPlayerDataFile(uuid, LoadFileType.PLAYER_DATA_READ_ONLY).getInt("interest");
    }

    public static int getLoanData(Player player) throws FileNotFoundException {
        return Math.abs(SkyBank.data.getInt("loan") - loadPlayerDataFile(player, LoadFileType.PLAYER_DATA_READ_ONLY).getInt("loan"));
    }

    public static int getLoanData(UUID uuid) throws FileNotFoundException {
        return  SkyBank.data.getInt("loan") - loadPlayerDataFile(uuid, LoadFileType.PLAYER_DATA_READ_ONLY).getInt("loan");
    }

    public static int getIslandSize(Player player){
        Island island = getIsland(player);

        if (island == null) {
            SkyBank.logger.severe("No island found");
            API.sendColoredMessage(player, Language.no_island_found);
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

    public static FileConfiguration loadPlayerDataFile(Player player, LoadFileType type) throws FileNotFoundException {
        return API.loadFile("PlayerData/" + (player).getUniqueId() + ".yml", type);
    }

    public static FileConfiguration loadPlayerDataFile(UUID uuid, LoadFileType type) throws FileNotFoundException {
        return API.loadFile("PlayerData/" + uuid + ".yml", type);
    }

    public static void savePlayerDataFile(Player player, FileConfiguration data){
        API.saveFile(data, "PlayerData/" + (player).getUniqueId() + ".yml");
    }

    public static void savePlayerDataFile(UUID uuid, FileConfiguration data){
        API.saveFile(data, "PlayerData/" + uuid + ".yml");
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

    public static void setTax(Player player, TaxType type) throws FileNotFoundException {
        if(type == TaxType.PAID){
            FileConfiguration data = loadPlayerDataFile(player, LoadFileType.PLAYER_DATA_READ_ONLY);
            data.set("tax", SkyBank.data.getInt("tax"));
            savePlayerDataFile(player, data);
        }
    }

    public static void setTax(UUID uuid, TaxType type) throws FileNotFoundException {
        if(type == TaxType.PAID){
            FileConfiguration data = loadPlayerDataFile(uuid, LoadFileType.PLAYER_DATA_READ_ONLY);
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

    public static double getInterestPercent(Player player){

        double balance = getBalance(player);

        List<Map<?, ?>> list = SkyBank.config.getMapList("interests");
        for(Map<?, ?> map : list){
            if(balance >= Double.parseDouble(String.valueOf(map.get("min-money"))) && balance <= Double.parseDouble(String.valueOf(map.get("max-money"))) && player.hasPermission((String) map.get("permission"))){
                return Double.parseDouble(String.valueOf(map.get("interest")));
            }
        }
        return 0;
    }

    public static double getInterestPercent(Player player, List<Map<?, ?>> list){

        double balance = getBalance(player);

        for(Map<?, ?> map : list){
            if(balance >= Double.parseDouble(String.valueOf(map.get("min-money"))) && balance <= Double.parseDouble(String.valueOf(map.get("max-money"))) && player.hasPermission((String) map.get("permission"))){
                return Double.parseDouble(String.valueOf(map.get("interest")));
            }
        }
        return 0;
    }

    public static double getInterest(Player player){
        return getInterestPercent(player) * getBalance(player) / 100;
    }

    public static double getInterest(Player player, List<Map<?, ?>> list){
        return getInterestPercent(player, list) * getBalance(player) / 100;
    }

    public static double getInterest(Player player, double interestPercent) throws FileNotFoundException {
        return interestPercent * getBankBalance(player) / 100;
    }

    public static double getBankBalance(Player player) throws FileNotFoundException {
        return loadPlayerDataFile(player, LoadFileType.PLAYER_DATA_READ_ONLY).getDouble("bank-balance");
    }

    public static double getBankBalance(UUID uuid) throws FileNotFoundException {
        return loadPlayerDataFile(uuid, LoadFileType.PLAYER_DATA_READ_ONLY).getDouble("bank-balance");
    }

    public static void setBankBalance(Player player, double balance) throws FileNotFoundException {
        FileConfiguration data = API.loadPlayerDataFile(player, LoadFileType.PLAYER_DATA_READ_ONLY);
        data.set("bank-balance", balance);
        API.savePlayerDataFile(player, data);
    }

    public static void setBankBalance(UUID uuid, double balance) throws FileNotFoundException {
        FileConfiguration data = API.loadPlayerDataFile(uuid, LoadFileType.PLAYER_DATA_READ_ONLY);
        data.set("bank-balance", balance);
        API.savePlayerDataFile(uuid, data);
    }

    public static void addBankBalance(Player player, double balance) throws FileNotFoundException {
        FileConfiguration data = API.loadPlayerDataFile(player, LoadFileType.PLAYER_DATA_READ_ONLY);
        data.set("bank-balance", data.getInt("bank-balance") + balance);
        API.savePlayerDataFile(player, data);
    }

    public static void addBankBalance(UUID uuid, double balance) throws FileNotFoundException {
        FileConfiguration data = API.loadPlayerDataFile(uuid, LoadFileType.PLAYER_DATA_READ_ONLY);
        data.set("bank-balance", data.getInt("bank-balance") + balance);
        API.savePlayerDataFile(uuid, data);
    }

    public static void removeBankBalance(UUID uuid, double balance) throws FileNotFoundException {
        FileConfiguration data = API.loadPlayerDataFile(uuid, LoadFileType.PLAYER_DATA_READ_ONLY);
        data.set("bank-balance", data.getInt("bank-balance") - balance);
        API.savePlayerDataFile(uuid, data);
    }

    public static void removeBankBalance(Player player, double balance) throws FileNotFoundException {
        FileConfiguration data = API.loadPlayerDataFile(player.getUniqueId(), LoadFileType.PLAYER_DATA_READ_ONLY);
        data.set("bank-balance", data.getInt("bank-balance") - balance);
        API.savePlayerDataFile(player.getUniqueId(), data);
    }

    public static Map<?, ?> getBankLoan(String name){

        List<Map<?, ?>> list = SkyBank.config.getMapList("loans");
        for(Map<?, ?> map : list){
            //System.out.println("[" + map.get("name") + "]" + " [" + name + "]");
            if(name.equalsIgnoreCase((String) map.get("name"))){
                //System.out.println("Fount it");
                return map;
            }
        }
        return null;
    }

    public static double getHoursPlayed(Player player){
        return player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 60.0;
    }

    public static ArrayList<String> getLoans(){
        ArrayList<String> output = new ArrayList<>();

        List<Map<?, ?>> list = SkyBank.config.getMapList("loans");
        for(Map<?, ?> map : list){
            output.add((String) map.get("name"));
        }
        return output;
    }

    public static boolean getLoanStatus(Player player, String loan){

        if (!loanedPLayersNames.contains(player.getUniqueId().toString()))
            return true;

        return !((List<String>) loanedPLayers.get(loanedPLayersNames.indexOf(player.getUniqueId().toString())).get("loan")).contains(loan);

        //return !((List<String>) loanedPLayers.get(loanedPLayersNames.indexOf(player.getUniqueId())).get(getLoans())).contains(loan);
    }

    public static String getBeautifiedLoanStatus(Player player, String loan){
        //System.out.println(getLoanStatus(player, loan));
        if (getLoanStatus(player, loan))
            return Language.loan_available;
        else
            return Language.loan_unavailable;
    }

    public static Map<?, ?> getIpLogTemplate(Player player){
        Map<String, String> map = new HashMap<>();

        map.put("uuid", player.getUniqueId().toString());
        map.put("name", player.getName());
        map.put("ip", player.getAddress().getHostName());

        return map;
    }

    public static Map<?, ?> getBankLoanTemplate(Player player, double toPay, List<String> loans){
        Map<String, List<String>> map = new HashMap<>();

        map.put("uuid", Collections.singletonList(player.getUniqueId().toString()));
        map.put("name", Collections.singletonList(player.getName()));
        map.put("to-pay", Collections.singletonList(String.valueOf(toPay)));
        map.put("loan", loans);

        return map;
    }

    public static Map<?, ?> getBankLoanTemplate(Player player, double toPay, String loan, Map<?, ?> oldTemplate){
        Map<String, List<String>> map = new HashMap<>();
        List<String> loans = new ArrayList<>((List<String>) oldTemplate.get("loan"));
        loans.add(loan);

        toPay += toPay * config.getDouble("loan-interest-on-take") / 100;

        map.put("uuid", Collections.singletonList(player.getUniqueId().toString()));
        map.put("name", Collections.singletonList(player.getName()));
        map.put("to-pay", Collections.singletonList(String.valueOf(toPay + Double.parseDouble(((List<String>) oldTemplate.get("to-pay")).get(0)))));
        map.put("loan", loans);

        return map;
    }

    public static double getAmountToPayForLoans(Player player){
        int index = loanedPLayersNames.indexOf(player.getUniqueId().toString());

        if(index == -1)
            return 0;

        return Double.parseDouble(((List<String>) loanedPLayers.get(index).get("to-pay")).get(0));
    }

    public static void addLoanByInterest(Player player){
        Map<String, List<String>> map = (Map<String, List<String>>) API.getBankLoan(player);
        map.put("to-pay", Collections.singletonList(String.valueOf(getAmountToPayForLoans(player) + getAmountToPayForLoans(player) * getInterestPercent(player) / 100)));
        setBankLoan(player, map);
    }

    public static void addLoan(Player player, double amount){
        Map<String, List<String>> map = (Map<String, List<String>>) API.getBankLoan(player);
        map.put("to-pay", Collections.singletonList(String.valueOf(getAmountToPayForLoans(player) + amount)));
        setBankLoan(player, map);
    }

    public static void removeLoan(Player player, double amount){
        Map<String, List<String>> map = (Map<String, List<String>>) API.getBankLoan(player);
        map.put("to-pay", Collections.singletonList(String.valueOf(getAmountToPayForLoans(player) - amount)));
        setBankLoan(player, map);
    }

    public static Map<?, ?> getBankLoan(Player player){
        return loanedPLayers.get(loanedPLayersNames.indexOf(player.getUniqueId().toString()));
    }

    public static void setBankLoan(Player player, Map<?, ?> map){
        if(!loanedPLayersNames.contains(player.getUniqueId().toString())){
            loanedPLayersNames.add(player.getUniqueId().toString());
            loanedPLayers.add(map);
        }
        else{
            int index =  loanedPLayersNames.indexOf(player.getUniqueId().toString());
            loanedPLayers.set(index, map);
        }

    }

    /*
      - player: "someUUID"
    name: "playerName"
    to-pay: 0
    loan:
      - "Loan1"
      - "Loan2"
      - "Loan3"
     */

}
