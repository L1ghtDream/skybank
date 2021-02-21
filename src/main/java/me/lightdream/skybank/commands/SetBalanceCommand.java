package me.lightdream.skybank.commands;

import me.lightdream.skybank.enums.LoadFileType;
import me.lightdream.skybank.exceptions.FileNotFoundException;
import me.lightdream.skybank.utils.Language;
import me.lightdream.skybank.utils.API;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public class SetBalanceCommand extends BaseCommand {

    public SetBalanceCommand() {
        forcePlayer = true;
        commandName = "setBalance";
        argLength = 2;
        usage = "<amount> <player>";
    }

    @Override
    public boolean run() throws FileNotFoundException {

        int amount;

        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            API.sendColoredMessage(player, Language.invalid_number_format);
            return true;
        }

        UUID returnPlayer = Bukkit.getOfflinePlayer(args[2]).getUniqueId();

        try{

            FileConfiguration data = API.loadPlayerDataFile(returnPlayer, LoadFileType.PLAYER_DATA_READ_ONLY);
            data.set("bank-balance", amount);

            API.savePlayerDataFile(returnPlayer, data);
            API.sendColoredMessage(player, Language.balance_updated);

        } catch (FileNotFoundException e) {
            API.sendColoredMessage(player, Language.player_does_not_exist);
            return true;
        }

        return true;
    }

}
