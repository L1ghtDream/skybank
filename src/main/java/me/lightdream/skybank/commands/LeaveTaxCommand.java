package me.lightdream.skybank.commands;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.enums.LoadFileType;
import me.lightdream.skybank.exceptions.FileNotFoundException;
import me.lightdream.skybank.utils.Language;
import me.lightdream.skybank.utils.API;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;
import world.bentobox.bentobox.database.objects.Island;

public class LeaveTaxCommand extends BaseCommand{

    public LeaveTaxCommand() {
        forcePlayer = true;
        commandName = "leaveTax";
        argLength = 0;
    }

    @Override
    public boolean run() throws FileNotFoundException {

        FileConfiguration data = API.loadPlayerDataFile(player, LoadFileType.PLAYER_DATA_READ_ONLY);

        double taxValue = SkyBank.config.getDouble("leave-tax");
        double taxPrice = API.getBalance(player) * taxValue / 100;

        Island island = API.getIsland(player);

        if(island != null){
            API.sendColoredMessage(player, Language.not_on_an_island);
            return true;
        }

        if(data.getBoolean("leave-tax") || API.getTaxData(island.getOwner()) < SkyBank.config.getInt("tax-limit")){
            API.sendColoredMessage(player, Language.already_have_leave_tax);
            return true;
        }

        if(args.length == 1){
            API.sendColoredMessage(player, Language.leave_tax_percent.replace("%tax%", String.valueOf(taxValue)));
            API.sendColoredMessage(player, Language.leave_tax_value.replace("%tax%", String.valueOf(taxPrice)));

            //TODO: Display a GUI
        }
        else if(args.length == 2){
            if(args[1].equalsIgnoreCase("pay")){

                double balance = API.getBalance(player);

                if(balance >= taxValue){

                    balance -= taxValue;

                    data.set("leave-tax", true);
                    API.savePlayerDataFile(player, data);

                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            FileConfiguration data = null;
                            try {
                                data = API.loadPlayerDataFile(player, LoadFileType.PLAYER_DATA_READ_ONLY);
                            } catch (FileNotFoundException e) {
                                SkyBank.logger.severe("Exception where no exception was expected");
                                e.printStackTrace();
                            }
                            data.set("leave-tax", false);
                            API.savePlayerDataFile(player, data);

                        }
                    }.runTaskLater(SkyBank.INSTANCE, SkyBank.config.getInt("leave-tax-timeout") * 20L);

                    API.removeBalance(player, balance);

                    API.sendColoredMessage(player, Language.paid_taxes);
                }
                else{
                    API.sendColoredMessage(sender, Language.not_enough_money);
                }
            }
        }





        return true;
    }

}
