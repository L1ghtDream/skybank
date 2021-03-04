package me.lightdream.skybank.commands;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.utils.API;
import me.lightdream.skybank.utils.Language;

public class ReloadCommand extends BaseCommand{

    public ReloadCommand() {
        forcePlayer = false;
        commandName = "reload";
        argLength = 0;
    }

    @Override
    public boolean run() {

        SkyBank.reload();
        API.sendColoredMessage(player, Language.reload_completed);

        return true;
    }
}
