package me.lightdream.skybank.commands;

import me.lightdream.skybank.SkyBank;
import me.lightdream.skybank.enums.LoadFileType;
import me.lightdream.skybank.utils.Language;
import me.lightdream.skybank.utils.Utils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import world.bentobox.bentobox.BentoBox;
import world.bentobox.bentobox.api.user.User;
import world.bentobox.bentobox.database.objects.Island;

import java.util.List;

import static me.lightdream.skybank.SkyBank.bentoBox;
import static me.lightdream.skybank.SkyBank.skyblock;

public class TaxCommand extends BaseCommand{

    //skybank tax
    //skybank tax pay
    public TaxCommand() {
        forcePlayer = true;
        commandName = "tax";
        argLength = 0;
    }

    @Override
    public boolean run() {

        if(args.length == 1){
            int tax = getTax(player);
            int size = getSize(player);
            double taxValue = getTaxValue(player, size);
            double taxPrice = getTaxPrice(player, taxValue);

            System.out.println(Language.unpaid_taxes);
            System.out.println(tax);
            System.out.println(Language.unpaid_taxes.replace("%tax%", String.valueOf(tax)));
            Utils.sendColoredMessage(sender, Language.unpaid_taxes.replace("%tax%", String.valueOf(tax)));
            Utils.sendColoredMessage(sender, Language.island_size.replace("%size%", String.valueOf(size)));
            Utils.sendColoredMessage(sender, Language.tax_value.replace("%tax%", String.valueOf(taxValue)));
            Utils.sendColoredMessage(sender, Language.tax_price.replace("%tax%", String.valueOf(taxPrice)));

            //TODO: Display a GUI
        }
        else if(args.length == 2){
            if(args[1].equalsIgnoreCase("pay")){
                double balance = getBalance(player);
                balance -= getTaxPrice(player);
                FileConfiguration data = getDataFile(player);
                data.set("tax", SkyBank.data.getInt("tax"));
                setBalance(player, balance);
            }
        }


        return true;
    }

    private int getTax(Player player){
        return  SkyBank.data.getInt("tax") - getDataFile(player).getInt("tax");
    }

    private int getSize(Player player){
        Island island = bentoBox.getIslands().getIsland(skyblock.getOverWorld(), User.getInstance(player));

        if (island == null) {
            Utils.sendPrefixedMessage(sender, Language.no_island_found);
            return 0;
        }

        return island.getProtectionRange() * 2;
    }

    private double getTaxValue(Player player){

        int size = getSize(player);

        List<String> taxes = (List<String>) SkyBank.config.getList("border-tax");

        for(String var1 : taxes){
            String[] var2 = var1.split(":");
            if(Integer.parseInt(var2[0]) == size){
                return Double.parseDouble(var2[1]);
            }
            else if (Integer.parseInt(var2[0]) >= size){
                break;
            }
        }
        return Double.parseDouble(taxes.get(0).split(":")[1]);
    }

    private double getTaxValue(Player player, int size){

        List<String> taxes = (List<String>) SkyBank.config.getList("border-tax");

        for(String var1 : taxes){
            String[] var2 = var1.split(":");
            if(Integer.parseInt(var2[0]) == size){
                return Double.parseDouble(var2[1]);
            }
            else if (Integer.parseInt(var2[0]) >= size){
                break;
            }
        }
        return Double.parseDouble(taxes.get(0).split(":")[1]);
    }

    private double getTaxPrice(Player player){
        return getTaxValue(player) * getBalance(player) / 100;
    }

    private double getTaxPrice(Player player, double taxValue){
        return taxValue * getBalance(player) / 100;
    }

    private double getBalance(Player player){
        return SkyBank.economy.getBalance(player);
    }

    private FileConfiguration getDataFile(Player player){
        return Utils.loadConfig("PlayerData/" + (player).getUniqueId() + ".yml", LoadFileType.PLAYER_DATA);
    }

    private void setBalance(Player player, double balance){
        SkyBank.economy.depositPlayer(player, balance);
    }
}
