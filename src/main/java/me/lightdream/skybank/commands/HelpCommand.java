package me.lightdream.skybank.commands;

public class HelpCommand extends BaseCommand{

    public HelpCommand() {
        forcePlayer = false;
        commandName = "help";
        argLength = 0;
    }

    @Override
    public boolean run() {

        //TODO: Display help

        return true;
    }
}
