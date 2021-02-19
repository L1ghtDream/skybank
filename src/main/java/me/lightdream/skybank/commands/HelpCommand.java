package me.lightdream.skybank.commands;

import me.lightdream.skybank.utils.Utils;

public class HelpCommand extends BaseCommand{

    public HelpCommand() {
        forcePlayer = false;
        commandName = "help";
        argLength = 0;
    }

    @Override
    public boolean run() {

        //TODO: [DONE] Display help

        Utils.sendCommands(player);

        return true;
    }
}
