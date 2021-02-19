package me.lightdream.skybank.commands;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.utils.Language;
import me.lightdream.skybank.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;

public class LeaveTaxCommand extends BaseCommand{

    public LeaveTaxCommand() {
        forcePlayer = true;
        commandName = "leaveTax";
        argLength = 0;
    }

    @Override
    public boolean run() {

        //TODO: Implement the island leave tax
        //TODO: The island leave event must be valid 10m

        FileConfiguration data = Utils.getPlayerDataFile(player);

        double taxValue = SkyBank.config.getDouble("leave-tax");
        double taxPrice = Utils.getBalance(player) * taxValue / 100;

        if(!data.getBoolean("leave-tax") || Utils.getTax(Utils.getIsland(player).getOwner()) < SkyBank.config.getInt("tax-limit")){
            Utils.sendColoredMessage(player, Language.already_have_leave_tax);
            return true;
        }

        if(args.length == 1){
            Utils.sendColoredMessage(player, Language.leave_tax_percent.replace("%tax%", String.valueOf(taxValue)));
            Utils.sendColoredMessage(player, Language.leave_tax_value.replace("%tax%", String.valueOf(taxPrice)));

            //TODO: Display a GUI
        }
        else if(args.length == 2){
            if(args[1].equalsIgnoreCase("pay")){

                double balance = Utils.getBalance(player);

                if(balance >= taxValue){
                    //TODO: [DONE] Make the payment last 10m
                    balance -= taxValue;


                    data.set("leave-tax", true);
                    Utils.savePlayerDataFile(player, data);

                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            FileConfiguration data = Utils.getPlayerDataFile(player);
                            data.set("leave-tax", false);
                            Utils.savePlayerDataFile(player, data);

                        }
                    }.runTaskLater(SkyBank.INSTANCE, SkyBank.config.getInt("leave-tax-timeout") * 20L);

                    Utils.removeBalance(player, balance);

                    Utils.sendColoredMessage(player, Language.paid_taxes);
                }
                else{
                    //TODO: [DONE] Display a message
                    Utils.sendColoredMessage(sender, Language.not_enough_money);
                }
            }
        }


        return true;
    }

}
