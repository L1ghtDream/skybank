package me.lightdream.skybank.commands;

import me.lightdream.skybank.exceptions.FileNotFoundException;
import me.lightdream.skybank.utils.Language;
import me.lightdream.skybank.utils.API;
import org.bukkit.Bukkit;

public class DepositCommand extends BaseCommand{

    public DepositCommand() {
        forcePlayer = true;
        commandName = "deposit";
        argLength = 1;
        usage = "<amount>";
    }

    @Override
    public boolean run() throws FileNotFoundException {

        if(args.length == 2){
            int amount;

            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e){
                API.sendColoredMessage(player, Language.invalid_number_format);
                return true;
            }

            if(amount <= API.getBalance(player)){
                API.addBankBalance(player, amount);
                API.removeBalance(player, amount);
                API.sendColoredMessage(player, Language.balance_updated);
            }
            else {
                API.sendColoredMessage(player, Language.not_enough_money);
            }
        }
        else if (args.length >= 3) {
            try{
                int amount = Integer.parseInt(args[1]);

                API.addBankBalance(Bukkit.getOfflinePlayer(args[2]).getUniqueId(), amount);
                API.sendColoredMessage(player, Language.balance_updated);

            } catch (FileNotFoundException e) {
                API.sendColoredMessage(player, Language.player_does_not_exist);
                return true;
            }
            catch (NumberFormatException e) {
                API.sendColoredMessage(player, Language.invalid_number_format);
                return true;
            }
        }


        return true;
    }
}