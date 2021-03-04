package me.lightdream.skybank.commands;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.utils.API;
import me.lightdream.skybank.utils.Language;
import org.bukkit.configuration.file.FileConfiguration;

public class AutoPayCommand extends BaseCommand{

    public AutoPayCommand() {
        forcePlayer = false;
        commandName = "autopay";
        argLength = 0;
    }

    @Override
    public boolean run() {

        FileConfiguration data = API.loadPlayerDataFile(player.getUniqueId());
        data.set("auto-pay", !data.getBoolean("auto-pay"));
        API.savePlayerDataFile(player.getUniqueId(), data);
        API.sendColoredMessage(player, Language.auto_pay.replace("{state}", String.valueOf(!data.getBoolean("auto-pay"))));

        return true;
    }
}
