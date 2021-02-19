package me.lightdream.skybank.commands;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.utils.Language;
import me.lightdream.skybank.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;

public class TaxCommand extends BaseCommand{

    public TaxCommand() {
        forcePlayer = true;
        commandName = "tax";
        argLength = 0;
    }

    @Override
    public boolean run() {

        //TODO: [DONE] Make the taxPrice be influenced about the back log
        //TODO: [DONE] Read form config the "backlog-exceed-tax" and apply it on tax pay if the backlog is exceeded
        //TODO: [DONE] Check if the available balance is enough
        //TODO: [DONE] Implement the island leave tax
        //TODO: [DONE] The island leave event must be valid 10m


        //TODO: Test
        System.out.println(SkyBank.data.getInt("tax"));
        SkyBank.data.set("tax", 5);

        int tax = Utils.getTax(player);
        int size = Utils.getIslandSize(player); //TODO: If size = 0 show just the error message not the rest of display
        double taxValue = Utils.getTaxValue(player, size);
        double taxPrice = Utils.getTaxPrice(player, taxValue);
        double overtaxValue = 0;
        double overtaxPrice = 0;
        double totalPrice = 0;

        if(tax >= SkyBank.config.getInt("tax-limit")){
            overtaxValue = SkyBank.config.getDouble("over-tax");
            overtaxPrice = taxPrice * tax * overtaxValue / 100;
        }

        totalPrice = overtaxPrice + taxPrice * tax;

        if(args.length == 1){
            Utils.sendColoredMessage(player, Language.unpaid_taxes.replace("%tax%", String.valueOf(tax)));
            Utils.sendColoredMessage(player, Language.island_size.replace("%size%", String.valueOf(size)));
            Utils.sendColoredMessage(player, Language.tax_value.replace("%tax%", String.valueOf(taxValue)));
            Utils.sendColoredMessage(player, Language.tax_price.replace("%tax%", String.valueOf(taxPrice * tax)));
            Utils.sendColoredMessage(player, Language.overtax_percent.replace("%tax%", String.valueOf(overtaxValue)));
            Utils.sendColoredMessage(player, Language.overtax_price.replace("%tax%", String.valueOf(overtaxPrice)));
            Utils.sendColoredMessage(player, "");
            Utils.sendColoredMessage(player, Language.total_price.replace("%tax%", String.valueOf(totalPrice)));

            //TODO: Display a GUI
        }
        else if(args.length == 2){
            if(args[1].equalsIgnoreCase("pay")){

                double balance = Utils.getBalance(player);

                if(balance >= totalPrice){
                    //TODO: [DONE] Relief from all the constrains of tax limit exceed
                    balance -= totalPrice;

                    FileConfiguration data = Utils.getPlayerDataFile(player);
                    System.out.println(data.getInt("tax"));
                    data.set("tax", SkyBank.data.getInt("tax"));
                    System.out.println(SkyBank.data.getInt("tax"));
                    System.out.println(data.getInt("tax"));
                    if(data.getBoolean("over-tax")){
                        Utils.getIsland(player).setProtectionRange(data.getInt("before-sanction-size"));
                        data.set("before-sanction-size", 0);
                        data.set("over-tax", false);
                    }
                    Utils.savePlayerDataFile(player, data);

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
