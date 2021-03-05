package me.lightdream.skybank.listener;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.commands.BaseCommand;
import me.lightdream.skybank.commands.CommandHandler;
import me.lightdream.skybank.gui.GUIManager;
import me.lightdream.skybank.utils.API;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("NullableProblems")
public class CommandListener implements CommandExecutor/*, TabCompleter*/ {

	private final SkyBank plugin;
	private final CommandHandler cmdHandler;

	public CommandListener(SkyBank plugin, CommandHandler cmdHandler) {
		this.plugin = plugin;
		this.cmdHandler = cmdHandler;
	}

	// Handle sub-commands
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(args.length == 0){
			if(sender instanceof Player){
				Player player = (Player) sender;
				if(API.getGUIStatus(player.getUniqueId())){
					player.openInventory(GUIManager.getMainGUI(player));
					return true;
				}
			}

		}
		if (args.length == 0 || !cmdHandler.commandExists(args[0])) {
			API.sendCommands(sender);
		} else {
            BaseCommand command = cmdHandler.getCommand(args[0]);
            command.processCmd(plugin, sender, args);
        }
		return true;
	}


	//TODO: Implement tab auto complete
	/*
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
		if (args.length == 1) {
			ArrayList<String> matches = new ArrayList<>();
			for (BaseCommand cmd : cmdHandler.getCommands()) {
			    String name = cmd.commandName;
				if (StringUtil.startsWithIgnoreCase(name, args[0])) {
					if (sender.hasPermission("hg." + name))
						matches.add(name);
				}
			}
			return matches;
		} else if (args.length >= 2) {
			if (args[0].equalsIgnoreCase("team") && sender instanceof Player) {
				if (args.length == 2) {
					List<String> listTeam = new ArrayList<>();
					listTeam.add("invite");
					listTeam.add("accept");
					listTeam.add("create");
					if (sender.hasPermission("hg.team.tp")) {
					    listTeam.add("tp");
					}
					ArrayList<String> matchesTeam = new ArrayList<>();
					for (String name : listTeam) {
						if (StringUtil.startsWithIgnoreCase(name, args[1])) {
							matchesTeam.add(name);
						}
					}
					return matchesTeam;
				}
				return null;
			} else if (args[0].equalsIgnoreCase("delete") ||
					args[0].equalsIgnoreCase("debug") ||
					args[0].equalsIgnoreCase("stop") ||
					(args[0].equalsIgnoreCase("forcestart")) ||
					(args[0].equalsIgnoreCase("join")) ||
					(args[0].equalsIgnoreCase("setlobbywall")) ||
					(args[0].equalsIgnoreCase("setexit")) ||
					(args[0].equalsIgnoreCase("toggle")) ||
					(args[0].equalsIgnoreCase("bordercenter")) ||
					((args[0].equalsIgnoreCase("spectate")))) {
				ArrayList<String> matchesDelete = new ArrayList<>();
				if (args.length == 2) {
					if (args[0].equalsIgnoreCase("setexit") && StringUtil.startsWithIgnoreCase("all", args[1])) {
						matchesDelete.add("all");
					}
					for (Game game : plugin.getGames()) {
						String name = game.getGameArenaData().getName();
						if (StringUtil.startsWithIgnoreCase(name, args[1])) {
							matchesDelete.add(name);
						}
					}
					return matchesDelete;
				}
			} else if (args[0].equalsIgnoreCase("chestrefill")) {
				ArrayList<String> matchesDelete = new ArrayList<>();
				if (args.length == 2) {
					for (Game game : plugin.getGames()) {
						String name = game.getGameArenaData().getName();
						if (StringUtil.startsWithIgnoreCase(name, args[1])) {
							matchesDelete.add(name);
						}
					}
					return matchesDelete;
				}
				if (args.length == 3) {
					ArrayList<String> matchesChestRefill = new ArrayList<>();
					for (int i = 30; i <= 120; i = i + 30) {
						if (StringUtil.startsWithIgnoreCase(String.valueOf(i), args[2])) {
							matchesChestRefill.add(String.valueOf(i));
						}
						if (StringUtil.startsWithIgnoreCase("<time=seconds>", args[2])) {
							matchesChestRefill.add("<time=seconds>");
						}
					}
					return matchesChestRefill;
				}
			} else if (args[0].equalsIgnoreCase("bordersize")) {
				ArrayList<String> matchesDelete = new ArrayList<>();
				if (args.length == 2) {
					for (Game game : plugin.getGames()) {
						String name = game.getGameArenaData().getName();
						if (StringUtil.startsWithIgnoreCase(name, args[1])) {
							matchesDelete.add(name);
						}
					}
					return matchesDelete;
				}
				if (args.length == 3 && args[0].equalsIgnoreCase("bordersize")) {
					return Collections.singletonList("<size=diameter>");
				}
			} else if (args[0].equalsIgnoreCase("bordertimer")) {
				ArrayList<String> matchesDelete = new ArrayList<>();
				if (args.length == 2) {
					for (Game game : plugin.getGames()) {
						String name = game.getGameArenaData().getName();
						if (StringUtil.startsWithIgnoreCase(name, args[1])) {
							matchesDelete.add(name);
						}
					}
					return matchesDelete;
				}
				if (args.length == 3 && args[0].equalsIgnoreCase("bordertimer")) {
					return Collections.singletonList("<start=remaining seconds>");
				}
				if (args.length == 4 && args[0].equalsIgnoreCase("bordertimer")) {
					return Collections.singletonList("<end=remaining seconds>");
				}
			} else if (args[0].equalsIgnoreCase("create")) {
				ArrayList<String> matchesCreate = new ArrayList<>();
				switch (args.length) {
					case 2:
						if (StringUtil.startsWithIgnoreCase("<arena-name>", args[1]))
							matchesCreate.add("<arena-name>");
						break;
					case 3:
						if (StringUtil.startsWithIgnoreCase("<min-players>", args[2]))
							matchesCreate.add("<min-players>");
						break;
					case 4:
						if (StringUtil.startsWithIgnoreCase("<max-players>", args[3]))
							matchesCreate.add("<max-players>");
						break;
					case 5:
						if (StringUtil.startsWithIgnoreCase("<time-seconds>", args[4]))
							matchesCreate.add("<time-seconds>");
						break;
					case 6:
						if (StringUtil.startsWithIgnoreCase("<cost>", args[5]))
							matchesCreate.add("<cost>");
						break;
				}
				return matchesCreate;
			}
		}
		return Collections.emptyList();
	}


	 */

}
