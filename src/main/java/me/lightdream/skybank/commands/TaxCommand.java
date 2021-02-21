package me.lightdream.skybank.commands;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.enums.LoadFileType;
import me.lightdream.skybank.exceptions.FileNotFoundException;
import me.lightdream.skybank.utils.Language;
import me.lightdream.skybank.utils.API;
import org.bukkit.configuration.file.FileConfiguration;

public class TaxCommand extends BaseCommand{

    public TaxCommand() {
        forcePlayer = true;
        commandName = "tax";
        argLength = 0;
    }

    @Override
    public boolean run() throws FileNotFoundException {

        int tax = API.getTaxData(player);
        int size = API.getIslandSize(player);
        double taxValue = API.getTaxValue(player, size);
        double taxPrice = API.getTaxPrice(player, taxValue);
        double overtaxValue = 0;
        double overtaxPrice = 0;
        double totalPrice;

        if(size == 0){
            API.sendColoredMessage(player, Language.size_0_island);
            return true;
        }

        if(tax >= SkyBank.config.getInt("tax-limit")){
            overtaxValue = SkyBank.config.getDouble("over-tax");
            overtaxPrice = taxPrice * tax * overtaxValue / 100;
        }

        if(API.getIsland(player).getOwner() == player.getUniqueId()){
            overtaxPrice = 0;
            taxPrice = 0;
        }

        totalPrice = overtaxPrice + taxPrice * tax;

        if(args.length == 1){
            API.sendColoredMessage(player, Language.unpaid_taxes.replace("%tax%", String.valueOf(tax)));
            API.sendColoredMessage(player, Language.island_size.replace("%size%", String.valueOf(size)));
            API.sendColoredMessage(player, Language.tax_value.replace("%tax%", String.valueOf(taxValue)));
            API.sendColoredMessage(player, Language.tax_price.replace("%tax%", String.valueOf(taxPrice * tax)));
            API.sendColoredMessage(player, Language.overtax_percent.replace("%tax%", String.valueOf(overtaxValue)));
            API.sendColoredMessage(player, Language.overtax_price.replace("%tax%", String.valueOf(overtaxPrice)));
            API.sendColoredMessage(player, "");
            API.sendColoredMessage(player, Language.total_price.replace("%tax%", String.valueOf(totalPrice)));

            //TODO: Display a GUI
        }
        else if(args.length == 2){
            if(args[1].equalsIgnoreCase("pay")){

                double balance = API.getBalance(player);

                if(totalPrice <= 0){
                    API.sendColoredMessage(player, Language.something_went_wrong);
                }

                if(balance >= totalPrice){
                    balance -= totalPrice;

                    FileConfiguration data = API.loadPlayerDataFile(player, LoadFileType.PLAYER_DATA_READ_ONLY);
                    data.set("tax", SkyBank.data.getInt("tax"));

                    if(data.getBoolean("over-tax")){
                        API.getIsland(player).setProtectionRange(data.getInt("before-sanction-size"));
                        data.set("before-sanction-size", 0);
                        data.set("over-tax", false);
                    }

                    API.savePlayerDataFile(player, data);
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
