package me.lightdream.skybank.commands;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.exceptions.FileNotFoundException;

public class DebugCommand extends BaseCommand{

    public DebugCommand() {
        forcePlayer = false;
        commandName = "debug";
        argLength = 0;
    }

    @Override
    public boolean run() throws FileNotFoundException {

        SkyBank.data.set("taxes", SkyBank.data.getInt("taxes") + 1);
        System.out.println(SkyBank.data.getInt("taxes"));

        return true;
    }
}
