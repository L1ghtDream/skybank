package me.lightdream.skybank.commands;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.enums.LoadFileType;
import me.lightdream.skybank.exceptions.FileNotFoundException;
import me.lightdream.skybank.gui.GUIManager;
import me.lightdream.skybank.utils.Language;
import me.lightdream.skybank.utils.API;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class TaxCommand extends BaseCommand{

    public TaxCommand() {
        forcePlayer = true;
        commandName = "tax";
        argLength = 0;
    }

    @Override
    public boolean run() throws FileNotFoundException {

        //TODO: Fix the non taxing bug

        if(API.getGUIStatus(player)){
            player.openInventory(GUIManager.getTaxInventory(player));
            return true;
        }

        int tax = API.getTaxData(player);
        int size = API.getIslandSize(player);
        double taxValue = API.getTaxValue(player, size);
        double taxPrice = API.getTaxPrice(player, taxValue);
        double overtaxValue = API.getPlayerOvertaxValue(player);
        double overtaxPrice = API.getPlayerOvertaxPrice(player);
        double totalPrice = overtaxPrice + taxPrice * tax;

        if(size == 0){
            API.sendColoredMessage(player, Language.size_0_island);
            return true;
        }

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
                payTax(player, totalPrice);
            }
        }
        return true;
    }

    public static void payTax(Player player, double totalPrice) throws FileNotFoundException {
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
            API.sendColoredMessage(player, Language.not_enough_money);
        }

    }

}
