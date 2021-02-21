package me.lightdream.skybank.commands;

import me.lightdream.skybank.enums.LoadFileType;
import me.lightdream.skybank.exceptions.FileNotFoundException;
import me.lightdream.skybank.utils.API;
import me.lightdream.skybank.utils.Language;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

public class GUICommand extends BaseCommand{

    public GUICommand() {
        forcePlayer = true;
        commandName = "gui";
        argLength = 0;
    }

    @Override
    public boolean run() throws FileNotFoundException {

        FileConfiguration data = API.loadPlayerDataFile(player, LoadFileType.PLAYER_DATA_READ_ONLY);
        data.set("gui", !data.getBoolean("gui"));
        API.savePlayerDataFile(player, data);
        API.sendColoredMessage(player, Language.gui_status_updated.replace("%status%", String.valueOf(data.getBoolean("gui"))));

        return true;
    }
}