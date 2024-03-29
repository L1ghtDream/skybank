package me.lightdream.skybank.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import me.lightdream.skybank.utils.API;
import me.lightdream.skybank.utils.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class WithdrawCommand extends BaseCommand{

    public WithdrawCommand() {
        forcePlayer = true;
        commandName = "withdraw";
        argLength = 1;
        usage = "<amount>";
    }

    @Override
    public boolean run()  {

        if(args.length == 2)
            withdraw(player, args[1]);
        else if (args.length >= 3) {

            int amount;

            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e){
                API.sendColoredMessage(player, Language.invalid_number_format);
                return true;
            }

            OfflinePlayer returnPlayer = Bukkit.getOfflinePlayer(args[2]);

            if(!API.checkPlayerFileExistance(returnPlayer.toString())){
                API.sendColoredMessage(player, Language.player_does_not_exist);
                return true;
            }

            FileConfiguration data = API.loadPlayerDataFile(returnPlayer.getUniqueId());
            data.set("bank-balance", data.getInt("bank-balance") - amount);

            API.savePlayerDataFile(player.getUniqueId(), data);
            API.sendColoredMessage(player, Language.balance_updated);

        }

        return true;
    }

    public static void withdraw(Player player, String input){
        int amount;

        try {
            amount = Integer.parseInt(input);
        } catch (NumberFormatException e){
            API.sendColoredMessage(player, Language.invalid_number_format);
            return;
        }

        FileConfiguration data = API.loadPlayerDataFile(player.getUniqueId());

        if(data.getInt("bank-balance") >= amount){
            data.set("bank-balance", data.getInt("bank-balance") - amount);
            API.savePlayerDataFile(player.getUniqueId(), data);
            API.removeBalance(player.getUniqueId(), amount);
            API.sendColoredMessage(player, Language.balance_updated);
            API.logAction(API.processLogAction(player, String.valueOf(amount)));

        }

    }

}