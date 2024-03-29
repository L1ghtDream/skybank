package me.lightdream.skybank.commands;

import com.google.common.base.Preconditions;
import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.listener.CommandListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHandler {

    private final SkyBank plugin;
    private final Map<String, BaseCommand> COMMANDS;

    public CommandHandler(SkyBank plugin) {
        this.plugin = plugin;
        this.COMMANDS = new HashMap<>();
        loadCommands();
        registerPermissions();
        PluginCommand command = plugin.getCommand("skybank");
        if (command != null) {
            command.setExecutor(new CommandListener(plugin, this));
        }
    }

    private void loadCommands() {
        registerCommand(TaxCommand.class);
        registerCommand(LeaveTaxCommand.class);
        registerCommand(DepositCommand.class);
        registerCommand(WithdrawCommand.class);
        registerCommand(BalanceCommand.class);
        registerCommand(SetBalanceCommand.class);
        registerCommand(InterestCommand.class);
        registerCommand(LoanCommand.class);
        registerCommand(GUICommand.class);
        registerCommand(DebugCommand.class);
        registerCommand(ReloadCommand.class);
        registerCommand(AutoPayCommand.class);
    }

    private void registerCommand(Class<? extends BaseCommand> cmdClass) {
        try {
            BaseCommand command = cmdClass.newInstance();
            COMMANDS.put(command.commandName, command);
            for(String alias : command.aliases){
                COMMANDS.put(alias, command);
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void registerPermissions() {
        PluginManager pm = Bukkit.getPluginManager();
        COMMANDS.forEach((command, baseCmd) -> {
            String perm = "skybank." + command;
            if (pm.getPermission(perm) == null) {
                Permission permission = new Permission(perm);
                permission.setDescription(String.format("Grants user access to SkyBank Command command '/skybank %s'", command));
                permission.setDefault(baseCmd.permissionDefault);
                pm.addPermission(permission);
            }
        });
    }

    public boolean commandExists(String command) {
        return COMMANDS.containsKey(command);
    }


    public List<BaseCommand> getCommands() {
        return new ArrayList<>(COMMANDS.values());
    }

    public BaseCommand getCommand(String command) {
        Preconditions.checkArgument(COMMANDS.containsKey(command), "SkyBank command does not exist: '/skybank %s'", command);
        return COMMANDS.get(command);
    }

}
