package me.lightdream.skybank.commands;

import me.lightdream.skybank.utils.Language;
import me.lightdream.skybank.utils.API;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DepositCommand extends BaseCommand{

    public DepositCommand() {
        forcePlayer = true;
        commandName = "deposit";
        argLength = 1;
        usage = "<amount>";
    }

    @Override
    public boolean run() {

        if(args.length == 2)
            deposit(player, args[1]);
        else if (args.length >= 3) {
            try{
                if(!API.checkPlayerFileExistance(Bukkit.getOfflinePlayer(args[2]).getUniqueId().toString())){
                    API.sendColoredMessage(player, Language.player_does_not_exist);
                    return true;
                }

                int amount = Integer.parseInt(args[1]);

                API.addBankBalance(Bukkit.getOfflinePlayer(args[2]).getUniqueId(), amount);
                API.sendColoredMessage(player, Language.balance_updated);

            } catch (NumberFormatException e) {
                API.sendColoredMessage(player, Language.invalid_number_format);
                return true;
            }
        }


        return true;
    }

    public static void deposit(Player player, String input){
        int amount;

        try {
            amount = Integer.parseInt(input);
        } catch (NumberFormatException e){
            API.sendColoredMessage(player, Language.invalid_number_format);
            return;
        }

        if(amount <= API.getBalance(player.getUniqueId())){
            API.addBankBalance(player.getUniqueId(), amount);
            API.removeBalance(player.getUniqueId(), amount);
            API.sendColoredMessage(player, Language.balance_updated);
            API.logAction(API.processLogAction(player, String.valueOf(amount)));
        }
        else {
            API.sendColoredMessage(player, Language.not_enough_money);
        }
    }

}