package me.lightdream.skybank.commands;

import me.lightdream.skybank.utils.API;
import me.lightdream.skybank.utils.Language;
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
    public boolean run() {

        if(args.length == 1){
            API.sendColoredMessage(player, Language.own_balance.replace("%money%", String.valueOf(API.getBankBalance(player.getUniqueId()))));
        }
        else if (args.length >= 2){
            OfflinePlayer returnPlayer = Bukkit.getOfflinePlayer(args[1]);
            API.sendColoredMessage(player, Language.others_balance.replace("%money%", String.valueOf(API.getBankBalance(returnPlayer.getUniqueId()))).replace("%player%", returnPlayer.getName()));
        }

        return true;
    }
}