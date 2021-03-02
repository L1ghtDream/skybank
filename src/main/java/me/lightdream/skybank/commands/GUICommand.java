package me.lightdream.skybank.commands;

import me.lightdream.skybank.enums.LoadFileType;
import me.lightdream.skybank.utils.API;
import me.lightdream.skybank.utils.Language;
import org.bukkit.configuration.file.FileConfiguration;

public class GUICommand extends BaseCommand{

    public GUICommand() {
        forcePlayer = true;
        commandName = "gui";
        argLength = 0;
    }

    @Override
    public boolean run() {

        FileConfiguration data = API.loadPlayerDataFile(player.getUniqueId());
        data.set("gui", !data.getBoolean("gui"));
        API.savePlayerDataFile(player.getUniqueId(), data);
        API.sendColoredMessage(player, Language.gui_status_updated.replace("%status%", String.valueOf(data.getBoolean("gui"))));

        return true;
    }
}