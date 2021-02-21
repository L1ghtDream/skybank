package me.lightdream.skybank.commands;

import me.lightdream.skybank.exceptions.FileNotFoundException;
import me.lightdream.skybank.utils.Language;
import me.lightdream.skybank.utils.API;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;

public class BalanceCommand extends BaseCommand{

    public BalanceCommand() {
        forcePlayer = true;
        commandName = "balance";
        aliases = new ArrayList<>();
        aliases.add("bal");
        argLength = 0;
    }

    @Override
    public boolean run() throws FileNotFoundException {

        if(args.length == 1){
            API.sendColoredMessage(player, Language.own_balance.replace("%money%", String.valueOf(API.getBankBalance(player))));
        }
        else if (args.length >= 2){

            OfflinePlayer returnPlayer = Bukkit.getOfflinePlayer(args[1]);

            try{
                API.sendColoredMessage(player, Language.others_balance.replace("%money%", String.valueOf(API.getBankBalance(returnPlayer.getUniqueId()))).replace("%player%", returnPlayer.getName()));
            } catch (FileNotFoundException e) {
                API.sendColoredMessage(player, Language.player_does_not_exist);
                return true;
            }
        }

        return true;
    }
}